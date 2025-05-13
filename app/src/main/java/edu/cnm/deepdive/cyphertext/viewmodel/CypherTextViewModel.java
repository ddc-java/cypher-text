package edu.cnm.deepdive.cyphertext.viewmodel;

import android.util.Log;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import edu.cnm.deepdive.cyphertext.model.entity.Game;
import edu.cnm.deepdive.cyphertext.service.CypherTextRepository;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import javax.inject.Inject;

@HiltViewModel
public class CypherTextViewModel extends ViewModel implements DefaultLifecycleObserver {

  private static final String TAG = CypherTextViewModel.class.getSimpleName();
  private final CypherTextRepository cypherTextRepository;
  private final MutableLiveData<Game> game;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  @Inject
  public CypherTextViewModel(CypherTextRepository cypherTextRepository) {
    this.cypherTextRepository = cypherTextRepository;
    game = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
  }

  public void startGame() {
    throwable.setValue(null);
    Game game = new Game(0);  // FIXME: 5/6/2025 Get initial hints from preferences
    cypherTextRepository
        .startGame(game)
        .subscribe(
            this.game::postValue,
            this::postThrowable,
            pending
        );
  }

  public LiveData<Game> getGame() {
    return game;
  }

  private void postThrowable(Throwable throwable) {
    Log.e(TAG, throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }
}
