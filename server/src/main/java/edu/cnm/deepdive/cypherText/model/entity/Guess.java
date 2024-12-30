package edu.cnm.deepdive.cypherText.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.NonNull;

@Entity
public class Guess {

  @Id
  @GeneratedValue
  @Column(name = "guess_id")
  private Long id;

  @NonNull
  @Column(name = "external_key", nullable = false, updatable = false, unique = true, columnDefinition = "UUID")
  @JsonProperty(access = Access.READ_ONLY)
  private UUID key;

//  @NonNull
  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name = "game_id", nullable = false, updatable = false)
  @JsonIgnore
  private Game game;

//  @NonNull
  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @JsonIgnore
  private Instant time;

  @Column(nullable = false, updatable = false)
  @JsonProperty(access = JsonProperty.Access.READ_WRITE)
  @JsonUnwrapped
  private CypherPair cypherPair;

  public Long getId() {
    return id;
  }

  @NonNull
  public UUID getKey() {
    return key;
  }

  public void setId(Long id) {
    this.id = id;
  }

//  @NonNull
  public Game getGame() {
    return game;
  }

  public void setGame( Game game) {
    this.game = game;
  }

//  @NonNull
  public Instant getTime() {
    return time;
  }

  public CypherPair getCypherPair() {
    return cypherPair;
  }

  public void setCypherPair(int fromCp, int toCp) {
    this.cypherPair = new CypherPair(fromCp, toCp);
  }

  @PrePersist
  private void generateKey() {
    key = UUID.randomUUID();
  }
}
