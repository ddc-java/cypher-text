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
    // TODO: 12/24/2024 Add initial number of hints
    List<Game> currentGames = gameRepository.findCurrentGames(user);
    Game gameToPlay;

    if (!currentGames.isEmpty()) {
      gameToPlay = currentGames.getFirst();
    } else {
      int quotesLength = quoteRepository.findAll().size();
      gameToPlay = game;
      gameToPlay.setUser(user);

      long quoteId = rng.nextLong(quotesLength);
      Quote quoteById = quoteRepository.findQuoteById(quoteId);
      gameToPlay.setQuote(quoteById);

      String textToEncrypt = quoteById.getQuoteText().toUpperCase();
      gameCypher = createCypher();
      String encodedQuote = EncodeQuote(textToEncrypt, gameCypher);
      gameToPlay.setEncodedQuote(encodedQuote);

      persistCypher(gameCypher, gameToPlay, textToEncrypt);
      gameRepository.save(gameToPlay);

      setInitialHints(gameToPlay);
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
          // TODO: 12/24/2024 Check if game solved
          Guess guess = new Guess();
          guess.setGame(gm);
          int[] guessChars = guessDto.getGuessText()
              .toUpperCase()
              .codePoints()
              .filter(Character::isAlphabetic)
              .limit(2)
              .toArray();
          guess.setCypherPair(guessChars[0], guessChars[1]);
          return guessRepository.save(guess);
        })
        .orElseThrow();
    game.setDecodedQuote(DecodeCypher(game));
    return gameRepository.save(game);
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
                Guess cypherGuess = guessRepository.findByGameKeyAndFromChar(gameKey, cp)
                    .orElse(null);
                if (cypherGuess != null) {
                  decodedText.append(Character.toChars(cypherGuess.getCypherPair().getTo()));
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

  private void setInitialHints(Game gameToPlay) {
    int quoteLength = gameToPlay.getQuote().getQuoteText().length();
    int initialHintNum = gameToPlay.getInitialHints();
    int hintLimit = (initialHintNum >= quoteLength) ? quoteLength -1 : initialHintNum;
    for(int hintNum = 0; hintNum < hintLimit; hintNum++) {
      long hintLoc = rng.nextLong(gameCypher.size() + 1);
      GameCypherPair gcp;
      do{
        gcp = gameCypherPairRepository
            .findGameCypherPairByGameKeyAndId(gameToPlay.getKey(), hintLoc)
            .orElseThrow();
      } while(gcp.isHint());
      gcp.setHint(true);
      gameCypherPairRepository.save(gcp);
      Guess guess = new Guess();
      guess.setGame(gameToPlay);
      guess.setCypherPair(gcp.getCypherPair().getTo(), gcp.getCypherPair().getFrom());
      guessRepository.save(guess);
    }
  }
}
