package edu.cnm.deepdive.cypherText.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.util.HashMap;
import java.util.UUID;
import org.springframework.lang.NonNull;

@Entity
@Table(indexes = @Index(columnList = "game_id, gameCypherMap, userCypherMap"))
public class Game {

  @NonNull
  @Id
  @GeneratedValue
  @Column(name = "game_id", nullable = false, updatable = false)
  @JsonIgnore
  private Long id;

  private UUID key;

  private UUID userId;

  private Long quoteId;

  private boolean solved;

  // TODO: 12/9/2024 one to many
//  private HashMap<String, String> gameCypherMap = new HashMap<>();

  // TODO: 12/9/2024 one to many
//  private HashMap<String, String> userCypherMap = new HashMap<>();

  public boolean isSolved() {
    return false;
    // TODO: 12/9/2024 return the boolean value of the encrypted quote decrypted by the user cypher compared to the original quote.
  }

  @PrePersist
  private void generateKey() {
    key = UUID.randomUUID();
  }
}
