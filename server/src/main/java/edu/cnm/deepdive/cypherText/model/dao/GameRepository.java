package edu.cnm.deepdive.cypherText.model.dao;

import edu.cnm.deepdive.cypherText.model.entity.Game;
import edu.cnm.deepdive.cypherText.model.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GameRepository extends JpaRepository<Game, Long> {

  Optional<Game> findGameByKeyAndUser(UUID key, User user);

  List<Game> findGamesByUser(User user);

  @Query("SELECT g FROM Game as g WHERE NOT g.solved AND g.user = :user")
  List<Game> findCurrentGames(User user);


}
