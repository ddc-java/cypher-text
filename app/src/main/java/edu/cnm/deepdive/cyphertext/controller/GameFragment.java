package edu.cnm.deepdive.cyphertext.controller;

import android.os.Bundle;
import android.text.TextWatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.cyphertext.databinding.FragmentGameBinding;
import edu.cnm.deepdive.cyphertext.model.entity.Game;
import edu.cnm.deepdive.cyphertext.viewmodel.CypherTextViewModel;

/**
 * A simple {@link Fragment} subclass. Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
public class GameFragment extends Fragment {

  private static final String EMPTY = "";
  private static final String HINT_CHARACTER = "?";
  private static final String NON_CHAR_COMPARE = "[\\W_]+";
  private FragmentGameBinding binding;
  private CypherTextViewModel viewModel;
  private Game game;

  public GameFragment() {
    // Required empty public constructor
  }


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding = FragmentGameBinding.inflate(inflater, container, false);
    binding.encodedCharId.addTextChangedListener(
        (SimpleTextWatcher) (editable) -> {
          binding.specificHintButtonId.setClickable(!getEncodedChar().isEmpty());
          binding.decodeButtonId.setClickable(!getEncodedChar().isEmpty());
        });
    binding.decodedCharId.addTextChangedListener(
        (SimpleTextWatcher) (editable) -> {
          binding.wofHintButtonId.setClickable(!getDecodedChar().isEmpty());
          binding.decodeButtonId.setClickable(!getEncodedChar().isEmpty());
        });
    binding.decodeButtonId.setOnClickListener((v) -> submitGuess());
    binding.specificHintButtonId.setOnClickListener((v) -> getSpecificHint());
    binding.wofHintButtonId.setOnClickListener((v) -> getWofHint());
    binding.randomHintButtonId.setOnClickListener((v) -> getRandomHint());
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ViewModelProvider provider = new ViewModelProvider(requireActivity());
    viewModel = provider.get(CypherTextViewModel.class);
    getLifecycle().addObserver(viewModel);
    LifecycleOwner owner = getViewLifecycleOwner();
    viewModel
        .getGame()
        .observe(owner, game1 -> handleGame(view, game1));
  }

  private void handleGame(View view, Game game) {
    NavController navController = Navigation.findNavController(view);
    binding.decodedViewId.setText(game.getDecodedQuote());
    binding.encodedViewId.setText(game.getEncodedQuote());
    if (game.isSolved()) {
      navController.navigate(GameFragmentDirections.navigateDialog());
    }
  }

  private void submitGuess() {
    String guessText = getGuessText();
    viewModel.submitGuess(guessText);
  }

  private String getGuessText() {
    String guessText = getEncodedChar() + getDecodedChar();
    clearChars();
    return guessText;
  }

  private void getSpecificHint() {
    String hintChar = getEncodedChar();
    clearChars();
    viewModel.submitGuess(hintChar + HINT_CHARACTER);
  }

  private void getWofHint() {
    String hintChar = getDecodedChar();
    clearChars();
    viewModel.submitGuess(HINT_CHARACTER + hintChar);
  }

  private void getRandomHint() {
    clearChars();
    viewModel.submitGuess(HINT_CHARACTER + HINT_CHARACTER);
  }

  /**
   * @noinspection DataFlowIssue
   */
  private String getEncodedChar() {
    return binding.encodedCharId
        .getText()
        .toString()
        .replaceAll(NON_CHAR_COMPARE, EMPTY);
  }

  /**
   * @noinspection DataFlowIssue
   */
  private String getDecodedChar() {
    return binding.decodedCharId
        .getText()
        .toString()
        .replaceAll(NON_CHAR_COMPARE, EMPTY);
  }

  private void clearChars() {
    binding.encodedCharId.setText("");
    binding.decodedCharId.setText("");
  }

  @FunctionalInterface
  private interface SimpleTextWatcher extends TextWatcher {

    @Override
    default void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    default void onTextChanged(CharSequence s, int start, int before, int count) {
    }
  }
}