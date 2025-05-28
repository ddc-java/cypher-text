package edu.cnm.deepdive.cypherText.service;

import edu.cnm.deepdive.cypherText.model.dao.GameCypherPairRepository;
import edu.cnm.deepdive.cypherText.model.dao.GameRepository;
import edu.cnm.deepdive.cypherText.model.dao.GuessRepository;
import edu.cnm.deepdive.cypherText.model.dao.QuoteRepository;
import edu.cnm.deepdive.cypherText.model.dto.GuessDto;
import edu.cnm.deepdive.cypherText.model.entity.CypherPair;
import edu.cnm.deepdive.cypherText.model.entity.Game;
import edu.cnm.deepdive.cypherText.model.entity.GameCypherPair;
import edu.cnm.deepdive.cypherText.model.entity.Guess;
import edu.cnm.deepdive.cypherText.model.entity.Quote;
import edu.cnm.deepdive.cypherText.model.entity.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.random.RandomGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("service")
@Service
public class GameService implements AbstractGameService {

  private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final int[] ALPHABET_CP_ARRAY = ALPHABET.codePoints().toArray();
  private static final int LENGTH = ALPHABET.length();
  private static final Character ENCODED_CHAR = '_';
  private static final int HINT_CHAR_CP = '\u003F';
  private final GameRepository gameRepository;
  private final QuoteRepository quoteRepository;
  private final GuessRepository guessRepository;
  private final GameCypherPairRepository gameCypherPairRepository;
  private final RandomGenerator rng;
  private Map<Integer, Integer> gameCypher;

  @Autowired
  public GameService(GameRepository gameRepository, QuoteRepository quoteRepository,
      GuessRepository guessRepository,
      GameCypherPairRepository gameCypherPairRepository,
      RandomGenerator rng) {
    this.gameRepository = gameRepository;
    this.quoteRepository = quoteRepository;
    this.guessRepository = guessRepository;
    this.gameCypherPairRepository = gameCypherPairRepository;
    this.rng = rng;
  }

  @Override
  public Game startOrGetGame(Game game, User user) {
    // TODO: 1/10/2025 Allow user to choose hint character
    List<Game> currentGames = gameRepository.findCurrentGames(user);
    Game gameToPlay;

    if (!currentGames.isEmpty()) {
      gameToPlay = currentGames.getFirst();
    } else {
      gameToPlay = game;
      gameToPlay.setUser(user);
      gameToPlay.setNumMoves(0);
      gameToPlay.setNumHints(game.getInitialHints());

      Quote quoteById = quoteRepository.findRandomQuote();
      gameToPlay.setQuote(quoteById);

      String textToEncrypt = quoteById.getQuoteText().toUpperCase();
      gameCypher = createCypher();
      String encodedQuote = EncodeQuote(textToEncrypt, gameCypher);
      gameToPlay.setEncodedQuote(encodedQuote);

      persistCypher(gameCypher, gameToPlay, textToEncrypt);
      gameRepository.save(gameToPlay);

      setRandomHints(gameToPlay, gameToPlay.getInitialHints());
      gameToPlay.setDecodedQuote(DecodeCypher(gameToPlay));
    }
    return gameRepository.save(gameToPlay);
  }

  @Override
  public Game getGame(UUID gameKey, User user) {
    return gameRepository
        .findGameByKeyAndUser(gameKey, user)
        .orElseThrow();
  }

  @Override
  public Game submitGuess(UUID gameKey, GuessDto guessDto, User user) {
    Game game = gameRepository.findGameByKeyAndUser(gameKey, user).orElseThrow();
    gameRepository
        .findGameByKeyAndUser(gameKey, user)
        .map((gm) -> {
          if(gm.isSolved()){
            throw new GameAlreadySolvedException("Game is already solved");
          }
          Integer[] guessChars = guessDto.getGuessText()
              .toUpperCase()
              .codePoints()
              .filter(cp ->
                  Character.isAlphabetic(cp)
                      || cp == HINT_CHAR_CP)
              .limit(2)
              .boxed()
              .toArray(Integer[]::new);
          validateGuess(guessChars, game);
          Guess guess = new Guess();
          guess.setGame(gm);
          if (Character.isAlphabetic(guessChars[0])) {
            if (Character.isAlphabetic(guessChars[1])) {
              guess.setCypherPair(guessChars[0], guessChars[1]);
              return guessRepository.save(guess);
            } else {
              GameCypherPair gcp = gameCypherPairRepository.findGameCypherPairByGameKeyAndToCp(
                  gameKey, guessChars[0]).orElseThrow();
              gcp.setHint(true);
              gameCypherPairRepository.save(gcp);
              guess.setCypherPair(guessChars[0], gcp.getCypherPair().getFrom());
              return guessRepository.save(guess);
            }
          } else if (guessChars.length == 1 || guessChars[1] == HINT_CHAR_CP) {
            setRandomHints(game, 1);
          } else {
            GameCypherPair gcp = gameCypherPairRepository.findGameCypherPairByGameKeyAndFromCp(
                gameKey, guessChars[1]).orElseThrow();
            gcp.setHint(true);
            gameCypherPairRepository.save(gcp);
            guess.setCypherPair(gcp.getCypherPair().getTo(), guessChars[1]);
            return guessRepository.save(guess);
          }
          return gm;
        })
        .orElseThrow();
    game.setDecodedQuote(DecodeCypher(game));
    game.setNumHints(gameCypherPairRepository.findGameCypherPairsHints(game.getKey()).size());
    game.setNumMoves(guessRepository.findByGameKey(game.getKey()).size());
    if(game.isSolved()) {
      game.setSolved();
    }
    return gameRepository.save(game);
  }

  private void validateGuess(Integer[] guessChars, Game game) {
    if (guessChars[0] == null) {
      throw new IllegalGuessException(
          "Guess must contain either a pair of characters or the hint character.");
    }
    if (Character.isAlphabetic(guessChars[0]) && guessChars[1] == null) {
      throw new IllegalGuessException("Guess must have 2 characters to make a cypher guess.");
    }
    if (guessChars[0] != HINT_CHAR_CP
        && gameCypherPairRepository
        .findGameCypherPairByGameKeyAndToCp(game.getKey(), guessChars[0])
        .orElse(null) == null) {
      throw new IllegalGuessException(
          "First character of Guess Must be from the encoded cypher provided.");
    }
  }

  @Override
  public Guess getGuess(UUID gameKey, UUID guessKey, User user) {
    return guessRepository
        .findByGameKeyAndGuessKeyAnUser(gameKey, guessKey, user)
        .orElseThrow();
  }

  private Map<Integer, Integer> createCypher() {
    Map<Integer, Integer> cypher = new HashMap<>();
    int[] gameCypher = ALPHABET_CP_ARRAY.clone();
    int srcIndex;
    int tempValue;
    for (int destIndex = gameCypher.length - 1; destIndex >= 0; destIndex--) {
      do {
        srcIndex = rng.nextInt(destIndex + 1);
        // TODO: 2/24/2025 If last letter has not changed position, change it with another position.
      } while (gameCypher[srcIndex] == ALPHABET_CP_ARRAY[destIndex]);
      tempValue = gameCypher[srcIndex];
      gameCypher[srcIndex] = gameCypher[destIndex];
      gameCypher[destIndex] = tempValue;
      cypher.put(ALPHABET_CP_ARRAY[destIndex], gameCypher[destIndex]);
    }
    return cypher;
  }

  private String EncodeQuote(String textToEncrypt, Map<Integer, Integer> gameCypher) {
// TODO: 12/24/2024 turn into Stream

//    return textToEncrypt.codePoints()
//        .map(gameCypher::get)
//        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
//        .toString();
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < textToEncrypt.length(); i++) {
      int cp = Character.codePointAt(textToEncrypt, i);
      if (Character.isAlphabetic(cp)) {
        cp = gameCypher.get(cp);

      }
      builder.append(Character.toChars(cp));
    }
    return builder.toString();
  }

  private String DecodeCypher(Game game) {
    StringBuilder decodedText = new StringBuilder();
    UUID gameKey = game.getKey();
    game.getEncodedQuote()
        .codePoints()
        .forEach((cp) -> {
              if (Character.isAlphabetic(cp)) {
                List<Guess> cypherGuesses = guessRepository.findByGameKeyAndFromChar(gameKey, cp);
                if (!cypherGuesses.isEmpty()) {
                  decodedText.append(
                      Character.toChars(cypherGuesses.getLast().getCypherPair().getTo()));
                } else {
                  decodedText.append(ENCODED_CHAR);
                }
              } else {
                decodedText.append(Character.toChars(cp));
              }
            }
        );
    return decodedText.toString();
  }

  private void persistCypher(Map<Integer, Integer> cypher, Game game, String textToEncrypt) {
    textToEncrypt
        .codePoints()
        .filter(Character::isAlphabetic)
        .distinct()
        .forEach((cp) -> {
          GameCypherPair gcp = new GameCypherPair();
          CypherPair cypherPair = new CypherPair();
          gcp.setGame(game);
          cypherPair.setFrom(cp);
          cypherPair.setTo(cypher.get(cp));
          gcp.setCypherPair(cypherPair);
          gcp.setHint(false);
          game.appendGameCypher(gcp);
        });
  }

  private void setRandomHints(Game gameToPlay, int initialHintNum) {
    GameCypherPair[] gcpArray = gameCypherPairRepository.findGameCypherPairsByGameKey(gameToPlay.getKey()).toArray(new GameCypherPair[0]);
    int hintLimit = (initialHintNum >= gcpArray.length) ? gcpArray.length -1 : initialHintNum;
    for (int hintNum = 0; hintNum < hintLimit; hintNum++) {
      GameCypherPair gcp;
      do {
        int hintLoc = rng.nextInt(gcpArray.length);
        gcp = gcpArray[hintLoc];
      } while (gcp.isHint());
      gcp.setHint(true);
      gameCypherPairRepository.save(gcp);
      Guess guess = new Guess();
      guess.setGame(gameToPlay);
      guess.setCypherPair(gcp.getCypherPair().getTo(), gcp.getCypherPair().getFrom());
      guessRepository.save(guess);
    }
  }
}
