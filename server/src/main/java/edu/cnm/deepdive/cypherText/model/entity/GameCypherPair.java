package edu.cnm.deepdive.cypherText.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.lang.NonNull;

@Entity
public class GameCypherPair {

  @Id
  @GeneratedValue
  @Column(name = "gcp_id", nullable = false, updatable = false)
  @JsonIgnore
  private long id;

//  @NonNull
  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name = "game_id", nullable = false, updatable = false)
  @JsonIgnore
  private Game game;

//  @NonNull
  @JsonProperty(access = JsonProperty.Access.READ_WRITE)
  @JsonUnwrapped
  private CypherPair cypherPair;

  private boolean hint;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

//  @NonNull
  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
    this.game = game;
  }

//  @NonNull
  public CypherPair getCypherPair() {
    return cypherPair;
  }

  public void setCypherPair( CypherPair cypherPair) {
    this.cypherPair = cypherPair;
  }

  public boolean isHint() {
    return hint;
  }

  public void setHint(boolean hint) {
    this.hint = hint;
  }

}
