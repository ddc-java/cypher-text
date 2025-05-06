package edu.cnm.deepdive.cyphertext.model.entity;

public class Game {

  private final String id;

  private String encodedQuote;

  private final int initialHints;

  private String decodedQuote;

  private boolean solved;

  public Game(int initialHints) {
    id = null;
    this.initialHints = initialHints;
  }
}
