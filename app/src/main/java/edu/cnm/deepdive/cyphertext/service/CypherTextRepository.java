package edu.cnm.deepdive.cyphertext.service;

import edu.cnm.deepdive.cyphertext.model.entity.Game;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import javax.inject.Inject;

public class CypherTextRepository {

  private final CypherTextServiceProxy proxy;
  private final UserRepository userRepository;
  private final GoogleSignInService signInService;
  private final Scheduler scheduler;
  private Game game;

  @Inject
  public CypherTextRepository(CypherTextServiceProxy proxy, UserRepository userRepository,
      GoogleSignInService signInService, Scheduler scheduler) {
    this.proxy = proxy;
    this.userRepository = userRepository;
    this.signInService = signInService;
    this.scheduler = scheduler;
  }

  public Single<Game> startOrGetGame(Game game) {
    return signInService
        .refreshBearerToken()
        .observeOn(scheduler)
        .flatMap((token) -> proxy.startOrGetGame(game, token))
        .doOnSuccess(this::setGame);
  }

  private void setGame(Game game) {
    this.game = game;
  }
}
