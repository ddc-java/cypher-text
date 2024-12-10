package edu.cnm.deepdive.cypherText.service;

import edu.cnm.deepdive.cypherText.model.dao.UserRepository;
import edu.cnm.deepdive.cypherText.model.entity.User;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements AbstractUserService {

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User getOrCreate(String oauthKey, String displayName) {  // TODO: 2/29/2024 Add parameters for additional user profile info from the bearer token
    return userRepository
        .findUserByOauthKey(oauthKey)
        .orElseGet(()->{
          User user = new User();
          user.setOauthKey(oauthKey);
          user.setDisplayName(displayName);
          // TODO: 2/29/2024 Assign any additional fields of user.
          return userRepository.save(user);
        });
  }

  @Override
  public User getCurrentUser() {
    return (User) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();
  }

  @Override
  public User updateUser(User received) {
    return userRepository
        .findById(getCurrentUser().getId())
        .map((user)->{
          String displayName = received.getDisplayName();
          //noinspection ConstantValue
          if (displayName != null) {
            user.setDisplayName(displayName);
          }          
          return userRepository.save(user);
        })
        .orElseThrow();
  }

  @Override
  public Optional<User> get(UUID key, User requester) {
    return userRepository
        .findUserByKey(key);// TODO: 2/29/2024 Apply access control rules as appropriate 
  }

//  @Override
//  public boolean follow(UUID followingKey, User requester, boolean following) {
//    return userRepository
//        .findById(requester.getId())
//        .flatMap((user)-> userRepository
//            .findUserByKey(followingKey)
//            .map((followed)->{
//              if(following){
//                user.getFollowedUsers().add(followed);
//              } else {
//                user.getFollowedUsers().remove(followed);
//              }
//              return userRepository.save(user).getFollowedUsers().contains(followed);
//            }))
//        .orElseThrow();
//  }

//  @Override
//  public Set<User> getFollows(User requester) {
//    return userRepository
//        .findById(requester.getId())
//        .map(User::getFollowedUsers)
//        .orElseThrow();
//  }

//  @Override
//  public Set<User> getFollowers(User requester) {
//    return userRepository
//        .findById(requester.getId())
//        .map(User::getFollowingUsers)
//        .orElseThrow();
//  }

}
