package edu.cnm.deepdive.cypherText.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.NonNull;

@Entity
public class Game {

  @NonNull
  @Id
  @GeneratedValue
  @Column(name = "game_id", nullable = false, updatable = false)
  @JsonIgnore
  private Long id;

  @NonNull
  @Column(name = "external_key", nullable = false, updatable = false, unique = true, columnDefinition = "UUID")
  private UUID key;

  @NonNull
  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", nullable = false, updatable = true)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private User user;

  @NonNull
  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @JsonProperty(access = Access.READ_ONLY)
  private Instant created;

  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JsonIgnore
  @JoinColumn(name = "quote_id")
  private Quote quote;

  @JsonProperty(access = Access.READ_WRITE)
  private String encodedQuote;

  @JsonProperty(access = Access.READ_WRITE)
  private String decodedQuote;

  @OneToMany(mappedBy = "game", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JsonProperty(access = Access.READ_WRITE)
  @JsonIgnore
  private final List<GameCypherPair> gameCypher = new LinkedList<>();

  @Column(nullable = false, updatable = false)
//  @Min(0)
//  @Max(3)
  private int initialHints;

  private boolean solved;

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Long getId() {
    return id;
  }


  public UUID getKey() {
    return key;
  }

  public void setKey(UUID key) {
    this.key = key;
  }

  public Quote getQuote() {
    return quote;
  }

  public void setQuote(Quote quote) {
    this.quote = quote;
  }

  public String getEncodedQuote() {
    return encodedQuote;
  }

  public void setEncodedQuote(String encodedQuote) {
    this.encodedQuote = encodedQuote;
  }

  public String getDecodedQuote() {
    return decodedQuote;
  }

  public void setDecodedQuote(String decodedQuote) {
    this.decodedQuote = decodedQuote;
  }

  public List<GameCypherPair> getGameCypher() {
    return gameCypher;
  }

  public void appendGameCypher(GameCypherPair gameCypherPair) {
    this.gameCypher.add(gameCypherPair);
  }

  public int getInitialHints() {
    return initialHints;
  }

  public void setInitialHints(int initialHints) {
    this.initialHints = initialHints;
  }

  public boolean isSolved() {
    return getDecodedQuote().equals(getQuote().getQuoteText().toUpperCase());
  }

  public void setSolved() {
    this.solved = true;
  }

  @PrePersist
  private void generateKey() {
    key = UUID.randomUUID();
  }
}
