package edu.cnm.deepdive.cypherText.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import org.springframework.lang.NonNull;

@Embeddable
public class CypherPair {

//  @NonNull
  @Column(name = "from_char")
  private Integer from;

//  @NonNull
  @Column(name = "to_char")
  private Integer to;

  public CypherPair() {}

  public CypherPair(Integer from, Integer to) {
    this.from = from;
    this.to = to;
  }

  @NonNull
  public Integer getFrom() {
    return from;
  }

  public void setFrom(@NonNull Integer from) {
    this.from = from;
  }

  @NonNull
  public Integer getTo() {
    return to;
  }

  public void setTo(@NonNull Integer to) {
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
