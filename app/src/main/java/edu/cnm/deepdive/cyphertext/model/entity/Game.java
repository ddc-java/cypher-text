package edu.cnm.deepdive.cyphertext.model.entity;

public class Game {

  private final String id;

  private final String encodedQuote;

  private final int initialHints;

  private String decodedQuote;

  private boolean solved;

  public Game(String encodedQuote, int initialHints) {
    id = null;
    this.encodedQuote = encodedQuote;
    this.initialHints = initialHints;
  }
}
