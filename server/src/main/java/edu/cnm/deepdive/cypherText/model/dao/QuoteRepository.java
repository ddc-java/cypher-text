package edu.cnm.deepdive.cypherText.model.dao;

import edu.cnm.deepdive.cypherText.model.entity.Quote;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepository extends JpaRepository<Quote, Long> {

  Quote findQuoteById(Long id);

  @Override
  List<Quote> findAll();

}
