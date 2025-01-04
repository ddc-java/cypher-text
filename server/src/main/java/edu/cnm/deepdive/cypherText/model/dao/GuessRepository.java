package edu.cnm.deepdive.cypherText.model.dao;

import edu.cnm.deepdive.cypherText.model.entity.Guess;
import edu.cnm.deepdive.cypherText.model.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GuessRepository extends JpaRepository<Guess, Long> {

  @Query("SELECT gs FROM Guess  as gs "
      + "JOIN gs.game AS gm "
      + "JOIN gm.user "
      + "WHERE gm.key = :gameKey "
      + "AND gs.key = :guessKey "
      + "AND gm.user = :user")
  Optional<Guess> findByGameKeyAndGuessKeyAnUser(UUID gameKey, UUID guessKey, User user);

  @Query("SELECT gs FROM Guess  as gs "
      + "JOIN gs.game AS gm "
      + "WHERE gm.key = :gameKey "
      + "AND gs.cypherPair.from = :from_char")
  List<Guess> findByGameKeyAndFromChar(UUID gameKey, int from_char);

  List<Guess> findByGameKey(UUID gameKey);
}
