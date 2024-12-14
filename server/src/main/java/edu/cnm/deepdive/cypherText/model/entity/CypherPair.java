package edu.cnm.deepdive.cypherText.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.lang.NonNull;

//@Entity
public class CypherPair {

  @NonNull
  @Id
  @GeneratedValue
  @Column(name = "cypher_pair_id", nullable = false, updatable = false)
  @JsonIgnore
  private Long id;

  @NonNull
  @ManyToOne
  @JoinColumn(name = "game_id", nullable = false, updatable = true)
  private Game game;

  @NonNull
  private Character from;

  @NonNull
  private Character to;

}
