package edu.cnm.deepdive.cypherText.model.dao;

import edu.cnm.deepdive.cypherText.model.entity.Game;
import edu.cnm.deepdive.cypherText.model.entity.GameCypherPair;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameCypherPairRepository extends JpaRepository<GameCypherPair, Long> {

//  List<GameCypherPair> findByGame(Game game);

}
