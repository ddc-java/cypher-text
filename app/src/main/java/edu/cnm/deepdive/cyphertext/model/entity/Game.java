package edu.cnm.deepdive.cyphertext.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Game {

  @SerializedName("key")
  @Expose
  private final String id;

  @Expose
  private String encodedQuote;

  @Expose
  private final int initialHints;

  @Expose
  private String decodedQuote;

  @Expose
  private boolean solved;

  public Game(int initialHints) {
    id = null;
    this.initialHints = initialHints;
  }

  public String getId() {
    return id;
  }

  public String getEncodedQuote() {
    return encodedQuote;
  }

  public String getDecodedQuote() {
    return decodedQuote;
  }

  public boolean isSolved() {
    return solved;
  }
}
