package edu.cnm.deepdive.cyphertext.controller;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.cyphertext.viewmodel.CypherTextViewModel;

public class EndGameDialogFragment extends DialogFragment {

  private CypherTextViewModel viewModel;

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    return new AlertDialog.Builder(requireContext())
        .setTitle("Congratulations!")
        .setMessage("You decoded the message!")
        .setMessage("Play again?")
        .setPositiveButton("Yes!", (dlg, which) -> viewModel.startGame())
        .setNegativeButton("No", (dlg, which) -> {
        })
        .create();
  }

  @Override
  public void onStart() {
    super.onStart();
    ViewModelProvider provider = new ViewModelProvider(requireActivity());
    viewModel = provider.get(CypherTextViewModel.class);
  }
}
