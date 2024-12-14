package edu.cnm.deepdive.cypherText.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cnm.deepdive.cypherText.model.dao.QuoteRepository;
import edu.cnm.deepdive.cypherText.model.entity.Quote;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Profile("preload")
@Component
public class PreloadService implements CommandLineRunner {
   private final QuoteRepository qRepo;
   private final String preloadFile;

  public PreloadService(QuoteRepository qRepo, @Value("${cypher-text.preload}") String preloadFile) {
    this.qRepo = qRepo;
    this.preloadFile = preloadFile;
  }

  private void preload() throws IOException {
    ClassPathResource resource = new ClassPathResource(preloadFile);
    try(InputStream input = resource.getInputStream()) {
      ObjectMapper mapper = new ObjectMapper();
      List<Quote> quotes = mapper.readValue(input, new TypeReference<>() {
      });
      qRepo.saveAll(quotes);
    }
  }

  @Override
  public void run(String... args) throws Exception {
    preload();
  }
}
