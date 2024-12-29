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
@RequestMapping("/games/{gameKey}/guesses")
public class GuessController {


  private final AbstractUserService userService;
  private final AbstractGameService gameService;

  @Autowired
  public GuessController(AbstractUserService userService, AbstractGameService gameService) {
    this.userService = userService;
    this.gameService = gameService;
  }

//  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//  public ResponseEntity<Game> post(@PathVariable UUID gameKey, @RequestBody GuessDto guessDto) {
//    Game createdGame = gameService.submitGuess(gameKey, guessDto, userService.getCurrentUser());
//    URI location = WebMvcLinkBuilder.linkTo(
//        WebMvcLinkBuilder.methodOn(getClass())
//            .get(createdGame.getKey())).toUri();
//    return ResponseEntity.created(location).body(createdGame);
//  }
//  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//  public ResponseEntity<Game> post(@PathVariable UUID gameKey, @RequestBody GuessDto guessDto) {
//    Guess newGuess = gameService.submitGuess(gameKey, guessDto, userService.getCurrentUser());
//    URI location = WebMvcLinkBuilder.linkTo(
//            WebMvcLinkBuilder.methodOn(GuessController.class)
//                .get(gameKey, newGuess.getKey())).toUri();
//    return ResponseEntity.created(location).body(newGuess);
//  }

//  @GetMapping(path = "/{guessKey}", produces = MediaType.APPLICATION_JSON_VALUE)
//  public Game get(@PathVariable UUID gameKey, @PathVariable UUID guessKey) {
//    return gameService.getGuess(gameKey, guessKey, userService.getCurrentUser());
//  }
// TODO: 12/26/2024 DO I need this GET endpoint now that I am returning a Game?

}


