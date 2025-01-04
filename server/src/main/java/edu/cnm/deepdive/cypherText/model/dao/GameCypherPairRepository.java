package edu.cnm.deepdive.cypherText.model.dao;

import com.fasterxml.jackson.annotation.OptBoolean;
import edu.cnm.deepdive.cypherText.model.entity.GameCypherPair;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameCypherPairRepository extends JpaRepository<GameCypherPair, Long> {

  List<GameCypherPair> findByGameId(Long game_id);

  @Query("SELECT gcp FROM GameCypherPair as gcp WHERE gcp.game.key = :game_key AND gcp.cypherPair.from = :from_cp")
  Optional<GameCypherPair> findGameCypherPairByGameKeyAndFromCp(UUID game_key, int from_cp);

  @Query("SELECT gcp FROM GameCypherPair as gcp WHERE gcp.game.key = :game_key AND gcp.cypherPair.to = :to_cp")
  Optional<GameCypherPair> findGameCypherPairByGameKeyAndToCp(UUID game_key, int to_cp);

  Optional<GameCypherPair> findGameCypherPairByGameKeyAndId(UUID game_key, Long gcp_id);

  List<GameCypherPair> findGameCypherPairByGameKey(UUID game_key);
}
