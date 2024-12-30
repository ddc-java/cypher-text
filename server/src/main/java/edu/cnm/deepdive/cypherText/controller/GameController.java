package edu.cnm.deepdive.cypherText.controller;

import edu.cnm.deepdive.cypherText.model.dto.GuessDto;
import edu.cnm.deepdive.cypherText.model.entity.Game;
import edu.cnm.deepdive.cypherText.model.entity.Guess;
import edu.cnm.deepdive.cypherText.service.AbstractGameService;
import edu.cnm.deepdive.cypherText.service.AbstractUserService;
import java.net.URI;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("service")
@RestController
@RequestMapping("/games")
public class GameController {

  private final AbstractUserService userService;
  private final AbstractGameService gameService;

  @Autowired
  public GameController(AbstractUserService userService, AbstractGameService gameService) {
    this.userService = userService;
    this.gameService = gameService;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Game> post(@RequestBody Game game) {
    Game createdGame = gameService.startOrGetGame(game, userService.getCurrentUser());
    URI location = WebMvcLinkBuilder.linkTo(
        WebMvcLinkBuilder.methodOn(getClass())
            .get(createdGame.getKey())).toUri();
    return ResponseEntity.created(location).body(createdGame);
  }

  @PostMapping(path = "/{gameKey}/guesses",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Game> post(@PathVariable UUID gameKey, @RequestBody GuessDto guessDto) {
    Game game = gameService.submitGuess(gameKey, guessDto, userService.getCurrentUser());
    URI location = WebMvcLinkBuilder.linkTo(
        WebMvcLinkBuilder.methodOn(getClass())
            .get(game.getKey())).toUri();
    return ResponseEntity.created(location).body(game);
  }

  @GetMapping(path = "/{gameKey}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Game get(@PathVariable UUID gameKey) {
    return gameService.getGame(gameKey, userService.getCurrentUser());
  }
}
