package edu.cnm.deepdive.cypherText.service;

import edu.cnm.deepdive.cypherText.model.entity.User;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface AbstractUserService {

  User getOrCreate(String oauthKey, String displayName);

  User getCurrentUser();

  User updateUser(User received);

  Optional<User> get(UUID key, User requester);

//  boolean follow(UUID followingKey, User requester, boolean following);

//  Set<User> getFollows(User requester);

//  Set<User> getFollowers(User requester);



}
