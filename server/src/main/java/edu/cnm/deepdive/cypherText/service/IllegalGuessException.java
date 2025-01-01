package edu.cnm.deepdive.cypherText.service;

public class IllegalGuessException extends IllegalArgumentException {

  public IllegalGuessException(String message) {
    super(message);
  }

  public IllegalGuessException() {
    super();
  }

  public IllegalGuessException(String message, Throwable cause) {
    super(message, cause);
  }

  public IllegalGuessException(Throwable cause) {
    super(cause);
  }
}
