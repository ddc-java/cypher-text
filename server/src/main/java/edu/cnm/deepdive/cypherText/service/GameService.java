package edu.cnm.deepdive.cypherText.service;

import edu.cnm.deepdive.cypherText.model.dao.GameCypherPairRepository;
import edu.cnm.deepdive.cypherText.model.dao.GameRepository;
import edu.cnm.deepdive.cypherText.model.dao.QuoteRepository;
import edu.cnm.deepdive.cypherText.model.dto.GuessDto;
import edu.cnm.deepdive.cypherText.model.entity.CypherPair;
import edu.cnm.deepdive.cypherText.model.entity.Game;
import edu.cnm.deepdive.cypherText.model.entity.GameCypherPair;
import edu.cnm.deepdive.cypherText.model.entity.Guess;
import edu.cnm.deepdive.cypherText.model.entity.Quote;
import edu.cnm.deepdive.cypherText.model.entity.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("service")
@Service
public class GameService implements AbstractGameService {

  private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final int[] ALPHABET_CP_ARRAY = ALPHABET.codePoints().toArray();
  private static final int LENGTH = ALPHABET.length();
  private final GameRepository gameRepository;
  private final QuoteRepository quoteRepository;
  private final GameCypherPairRepository gameCypherPairRepository;
  private final RandomGenerator rng;
  private Map<Integer, Integer> gameCypher;

  @Autowired
  public GameService(GameRepository gameRepository, QuoteRepository quoteRepository,
      GameCypherPairRepository gameCypherPairRepository,
      RandomGenerator rng) {
    this.gameRepository = gameRepository;
    this.quoteRepository = quoteRepository;
    this.gameCypherPairRepository = gameCypherPairRepository;
    this.rng = rng;
  }

  @Override
  public Game startOrGetGame(Game game, User user) {
    // TODO: 12/24/2024 Add initial number of hints
    List<Game> currentGames = gameRepository.findCurrentGames(user);
    Game gameToPlay;
    // TODO: 12/24/2024 Uncomment if statement

//    if (!currentGames.isEmpty()) {
//      gameToPlay = currentGames.getFirst();
//    } else {
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
//    }
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
    // TODO: 12/24/2024 Parse Guess
    // TODO: 12/24/2024 Find Game
    // TODO: 12/24/2024 Persist Guess

    return null;
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
          game.appendGameCypher(gcp);
        });
  }
}
