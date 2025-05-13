package edu.cnm.deepdive.cypherText.model.dao;

import edu.cnm.deepdive.cypherText.model.entity.Quote;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuoteRepository extends JpaRepository<Quote, Long> {

  Quote findQuoteById(Long id);

  @Override
  List<Quote> findAll();

  @Query("SELECT q FROM Quote AS q ORDER BY RAND() LIMIT 1")
  Quote findRandomQuote();

}
