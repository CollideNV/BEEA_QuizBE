package be.bewire.quiz.repository;

import be.bewire.quiz.repository.entity.LeaderboardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaderboardRepository extends JpaRepository<LeaderboardEntity, String> {
}
