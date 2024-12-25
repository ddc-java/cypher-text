package edu.cnm.deepdive.cypherText.service;

import edu.cnm.deepdive.cypherText.model.dto.GuessDto;
import edu.cnm.deepdive.cypherText.model.entity.Game;
import edu.cnm.deepdive.cypherText.model.entity.Guess;
import edu.cnm.deepdive.cypherText.model.entity.User;
import java.util.UUID;

public interface AbstractGameService {

  Game startOrGetGame(Game game, User user);

  Game getGame(UUID gameKey, User user);

  Guess submitGuess(UUID gameKey, GuessDto guessDto, User user);

  Guess getGuess(UUID gameKey, UUID guessKey, User currentUser);
}

