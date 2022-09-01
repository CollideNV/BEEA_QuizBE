package be.bewire.quiz.repository;

import be.bewire.quiz.repository.entity.LeaderboardEntity;
import be.bewire.quiz.repository.entity.ScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ScoreRepository extends JpaRepository<ScoreEntity, String> {
    void deleteScoreEntitiesByLeaderboardEntity(LeaderboardEntity entity);
}
