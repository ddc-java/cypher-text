package edu.cnm.deepdive.cypherText.model.dto;


public class GuessDto {

  public String guessText;


  public GuessDto() {}

  public GuessDto(String guessText) {
    this.guessText = guessText;
  }

  public String getGuessText() {
    return guessText;
  }

  public void setGuessText(String guessText) {
    this.guessText = guessText;
  }
}
