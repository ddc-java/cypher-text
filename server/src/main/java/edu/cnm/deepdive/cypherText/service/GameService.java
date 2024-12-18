package edu.cnm.deepdive.cypherText.service;

import edu.cnm.deepdive.cypherText.model.dao.GameCypherPairRepository;
import edu.cnm.deepdive.cypherText.model.dao.GameRepository;
import edu.cnm.deepdive.cypherText.model.dao.QuoteRepository;
import edu.cnm.deepdive.cypherText.model.entity.CypherPair;
import edu.cnm.deepdive.cypherText.model.entity.Game;
import edu.cnm.deepdive.cypherText.model.entity.GameCypherPair;
import edu.cnm.deepdive.cypherText.model.entity.Guess;
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
    List<Game> currentGames = gameRepository.findCurrentGames(user);
    Game gameToPlay;
//    if (!currentGames.isEmpty()) {
//      gameToPlay = currentGames.getFirst();
//    } else {
    int quotesLength = quoteRepository.findAll().size();
    gameToPlay = game;
    gameToPlay.setUser(user);
    gameToPlay.setQuote(quoteRepository.findQuoteById(rng.nextLong(quotesLength)));
    String textToEncrypt = gameToPlay.getQuote().getQuoteText().toUpperCase();
    gameCypher = createCypher(textToEncrypt);
    String encodedQuote = EncodeQuote(textToEncrypt, gameCypher);
    gameToPlay.setEncodedQuote(encodedQuote);
    gameRepository.save(gameToPlay);
    persistCypher(gameCypher, gameToPlay);
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
  public Game submitGuess(UUID gameKey, Guess guess, User user) {
    return null;
  }

  private Map<Integer, Integer> createCypher(String textToEncrypt) {
    Map<Integer, Integer> cypher = new HashMap<>();
    int srcIndex;
    int srcValue;
    int destValue;
    int tempValue;
    for(int destIndex = ALPHABET.length() - 1; destIndex >= 0; destIndex--) {
      srcIndex = rng.nextInt(destIndex);
      srcValue = ALPHABET.codePointAt(srcIndex);
      destValue = ALPHABET.codePointAt(destIndex);
      tempValue = srcValue;
      srcValue = destValue;
      destValue = tempValue;

    }

    for (int i = 0; i < textToEncrypt.length(); i++) {
      int cp = Character.codePointAt(textToEncrypt, i);
      int ecp;
      if (!cypher.containsKey(cp)) {
        do {
          ecp = Character.codePointAt(ALPHABET, rng.nextInt(LENGTH));
        } while (cypher.containsKey(ecp) || ecp == cp);
        cypher.put(cp, ecp);

      }
    }
    return cypher;
  }

  private String EncodeQuote(String textToEncrypt, Map<Integer, Integer> gameCypher) {

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

  private void persistCypher(Map<Integer, Integer> cypher, Game game) {
    GameCypherPair gcp = new GameCypherPair();
    CypherPair cypherPair = new CypherPair();
    for (Map.Entry<Integer, Integer> entry : cypher.entrySet()) {
      gcp.setGame(game);
      cypherPair.setFrom(entry.getKey());
      cypherPair.setTo(entry.getValue());
      gcp.setCypherPair(cypherPair);
      gameCypherPairRepository.save(gcp);
    }
  }
}
