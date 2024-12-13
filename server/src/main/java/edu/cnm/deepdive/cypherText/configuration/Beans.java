package edu.cnm.deepdive.cypherText.configuration;

import java.util.random.RandomGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beans {

  @Bean
  public RandomGenerator provideRandomGenerator() {
    return RandomGenerator.of("Xoroshiro128PlusPlus");
  }
}
