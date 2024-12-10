package edu.cnm.deepdive.cypherText.controller;

import edu.cnm.deepdive.cypherText.model.entity.User;
import edu.cnm.deepdive.cypherText.service.AbstractUserService;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  private final AbstractUserService userService;

  @Autowired
  public UserController(AbstractUserService userService) {
    this.userService = userService;
  }

  @GetMapping(path = "/me")
  public User get(){
    return userService.getCurrentUser();
  }

//  @GetMapping(path = "/me/follows", produces = MediaType.APPLICATION_JSON_VALUE)
//  public Set<User> getFollows () {
//    return userService.getFollows(userService.getCurrentUser());
//  }
//
//  @GetMapping(path = "me/followers", produces = MediaType.APPLICATION_JSON_VALUE)
//  public Set<User> getFollowers() {
//    return userService.getFollowers(userService.getCurrentUser());
//  }
//
//  @PutMapping(path = "/me/follows/{followedKey}",
//      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//  public boolean follow(@PathVariable UUID followedKey, @RequestBody boolean following){
//    return userService.follow(followedKey, userService.getCurrentUser(), following);
//  }
}
