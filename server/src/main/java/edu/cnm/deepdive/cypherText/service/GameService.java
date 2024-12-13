package edu.cnm.deepdive.cypherText.service;

import edu.cnm.deepdive.cypherText.model.dao.GameRepository;
import edu.cnm.deepdive.cypherText.model.dao.QuoteRepository;
import edu.cnm.deepdive.cypherText.model.dao.UserRepository;
import edu.cnm.deepdive.cypherText.model.entity.Game;
import edu.cnm.deepdive.cypherText.model.entity.User;
import java.util.List;
import java.util.UUID;
import java.util.random.RandomGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("service")
@Service
public class GameService implements AbstractGameService{

  private final GameRepository gameRepository;
  private final QuoteRepository quoteRepository;
  private final RandomGenerator rng;

  @Autowired
  public GameService(GameRepository gameRepository, QuoteRepository quoteRepository, RandomGenerator rng) {
    this.gameRepository = gameRepository;
    this.quoteRepository = quoteRepository;
    this.rng = rng;
  }

  @Override
  public Game startOrGetGame(Game game, User user) {
    List<Game> currentGames = gameRepository.findCurrentGames(user);
    Game gameToPlay;
//    if(!currentGames.isEmpty()) {
//      gameToPlay = currentGames.getFirst();
//    } else {
      int quotesLength = quoteRepository.findAll().size();
      gameToPlay = game;
      gameToPlay.setUser(user);
      gameToPlay.setQuote(quoteRepository.findQuoteById(rng.nextLong(quotesLength)));
//    }
    return gameRepository.save(gameToPlay);
  }

  @Override
  public Game getGame(UUID gameKey, User user) {
    return gameRepository
        .findGameByKeyAndUser(gameKey, user)
        .orElseThrow();
  }
}
