package edu.cnm.deepdive.cyphertext.service;

import edu.cnm.deepdive.cyphertext.model.entity.Game;
import edu.cnm.deepdive.cyphertext.model.entity.Guess;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javax.inject.Inject;

public class CypherTextRepository {

  private final CypherTextServiceProxy proxy;
  private final UserRepository userRepository;
  private final GoogleSignInService signInService;
  private final Scheduler scheduler;
  private Game game;

  @Inject
  public CypherTextRepository(CypherTextServiceProxy proxy, UserRepository userRepository,
      GoogleSignInService signInService) {
    this.proxy = proxy;
    this.userRepository = userRepository;
    this.signInService = signInService;
    scheduler = Schedulers.single();
  }

  public Single<Game> startGame(Game game) {
    return signInService
        .refreshBearerToken()
        .observeOn(scheduler)
        .flatMap((token) -> proxy.startGame(game, token))
        .doOnSuccess(this::setGame);
  }

  public Single<Game> getGame(String id) {
    return signInService
        .refreshBearerToken()
        .observeOn(scheduler)
        .flatMap((token) -> proxy.getGame(id, token))
        .doOnSuccess(this::setGame);
  }

  public Single<Game> submitGuess(Guess guess) {
    return signInService
        .refreshBearerToken()
        .observeOn(scheduler)
        .flatMap((token) -> proxy.submitGuess(game.getId(), guess, token))
        .subscribeOn(scheduler);
  }

  public Game getGame() {
    return game;
  }

  private void setGame(Game game) {
    this.game = game;
  }
}
