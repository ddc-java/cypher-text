package edu.cnm.deepdive.cyphertext.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Guess {

  @SerializedName("key")
  @Expose
  private final String id;

  @Expose
  private final String guessText;

  public Guess(String guessText) {
    id = null;
    this.guessText = guessText;
  }
}
