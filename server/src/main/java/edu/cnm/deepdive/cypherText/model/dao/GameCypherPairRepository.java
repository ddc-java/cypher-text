package edu.cnm.deepdive.cypherText.model.dao;

import edu.cnm.deepdive.cypherText.model.entity.GameCypherPair;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameCypherPairRepository extends JpaRepository<GameCypherPair, Long> {

  List<GameCypherPair> findByGameId(Long game_id);

  @Query("SELECT gcp FROM GameCypherPair as gcp WHERE gcp.game.id = :game_id AND gcp.cypherPair.from = :from_cp")
  List<GameCypherPair> findGameCypherPairByGameIdAndFromCp(Long game_id, int from_cp);
}
