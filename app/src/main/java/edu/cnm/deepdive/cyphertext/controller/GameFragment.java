package edu.cnm.deepdive.cyphertext.controller;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
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
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding = FragmentGameBinding.inflate(inflater, container, false);
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
        .observe(owner, this::handleGame);
  }

  private void handleGame(Game game) {
    CharSequence encodedQuote = game.getEncodedQuote();
//    CharSequence encodedQuote = "This is text";
    CharSequence decodedQuote = game.getDecodedQuote();
//    CharSequence decodedQuote = "How about more text";
    binding.decodedViewId.setText(decodedQuote);
    binding.encodedViewId.setText(encodedQuote);
  }
}