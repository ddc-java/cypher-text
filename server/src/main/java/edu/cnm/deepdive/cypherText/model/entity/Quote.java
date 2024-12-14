package edu.cnm.deepdive.cypherText.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@SuppressWarnings("JpaDataSourceORMInspection")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class Quote {

  @Id
  @GeneratedValue
  @Column(name = "quote_id", nullable = false, updatable = false)
  private Long id;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String quoteText;

  public Long getId() {
    return id;
  }

  public String getQuoteText() {
    return quoteText;
  }

  public void setQuoteText(String quote) {
    this.quoteText = quote;
  }
}
