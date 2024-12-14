package edu.cnm.deepdive.cypherText.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import org.springframework.lang.NonNull;

@Embeddable
public class CypherPair {

  @NonNull
  private Character from;

  @NonNull
  private Character to;

  public CypherPair() {}

  public CypherPair(Character from, Character to) {
    this.from = from;
    this.to = to;
  }

  @NonNull
  public Character getFrom() {
    return from;
  }

  public void setFrom(@NonNull Character from) {
    this.from = from;
  }

  @NonNull
  public Character getTo() {
    return to;
  }

  public void setTo(@NonNull Character to) {
    this.to = to;
  }

  @Override
  public int hashCode() {
    return Objects.hash(from, to);
  }

  @Override
  public boolean equals(Object obj) {
    boolean equals;
    if (this == obj) {
      equals = true;
    } else if (obj instanceof CypherPair other) {
      equals = (this.from == other.from && this.to == other.to);
    } else {
      equals = false;
    }
    return equals;
  }
}
