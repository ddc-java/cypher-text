package edu.cnm.deepdive.cypherText.service;

import edu.cnm.deepdive.cypherText.model.dao.GameRepository;
import edu.cnm.deepdive.cypherText.model.dao.QuoteRepository;
import edu.cnm.deepdive.cypherText.model.entity.Game;
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
  private final RandomGenerator rng;
  private Map<Integer, Integer> gameCypher;

  @Autowired
  public GameService(GameRepository gameRepository, QuoteRepository quoteRepository,
      RandomGenerator rng) {
    this.gameRepository = gameRepository;
    this.quoteRepository = quoteRepository;
    this.rng = rng;
  }

  @Override
  public Game startOrGetGame(Game game, User user) {
    List<Game> currentGames = gameRepository.findCurrentGames(user);
    Game gameToPlay;
    if (!currentGames.isEmpty()) {
      gameToPlay = currentGames.getFirst();
    } else {
      int quotesLength = quoteRepository.findAll().size();
      gameToPlay = game;
      gameToPlay.setUser(user);
      gameToPlay.setQuote(quoteRepository.findQuoteById(rng.nextLong(quotesLength)));
      String textToEncrypt = gameToPlay.getQuote().getQuoteText().toUpperCase();
      gameToPlay.setEncodedQuote(EncodeQuote(textToEncrypt));
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
  public Game submitMove(UUID gameKey, Guess guess, User user) {
//    Move currentMove = new Move();
    return null;
  }

  private String EncodeQuote(String textToEncrypt) {
    Map<Integer, Integer> encodeMap = new HashMap<>();
    for (int i = 0; i < textToEncrypt.length(); i++) {
      int cp = Character.codePointAt(textToEncrypt, i);
      int ecp;
      if (!encodeMap.containsKey(cp)) {
        do {
          ecp = Character.codePointAt(ALPHABET, rng.nextInt(LENGTH));
        } while (encodeMap.containsKey(ecp));
        encodeMap.put(cp, ecp);
      }
    }

    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < textToEncrypt.length(); i++) {
      int cp = Character.codePointAt(textToEncrypt, i);
      if (Character.isAlphabetic(cp)) {
        cp = encodeMap.get(cp);
      }
      builder.append(Character.toChars(cp));
    }
    return builder.toString();

//        .codePoints()
//        .filter(Character::isAlphabetic)
//        .distinct()
//        .boxed()
//        .collect(Collectors.toMap(Function.identity(), (codepoint) -> {
//          do {
//            int c = Character.codePointAt(ALPHABET, rng.nextInt(LENGTH));
//          } while (encodeMap.containsValue(c));
//          return codepoint;
//        }));
//    return encodeMap;
//    gameCypher = EncodeQuote(textToEncrypt);
//    String encodedQuote = textToEncrypt
//        .codePoints()
//        .map((codepoint)->{
//          if(Character.isAlphabetic(codepoint)){
//            codepoint = gameCypher.get(codepoint);
//          }
//          return codepoint;
//        })
//        .toString();
  }
}
