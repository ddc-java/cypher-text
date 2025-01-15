package edu.cnm.deepdive.cypherText.service;

public class GameAlreadySolvedException extends IllegalStateException {

  public GameAlreadySolvedException(String message) {
    super(message);
  }

  public GameAlreadySolvedException(Throwable cause) {
    super(cause);
  }

  public GameAlreadySolvedException(String message, Throwable cause) {
    super(message, cause);
  }

  public GameAlreadySolvedException() {
    super();
  }
}
