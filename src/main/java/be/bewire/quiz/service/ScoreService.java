package be.bewire.quiz.service;

import be.bewire.quiz.model.Score;
import be.bewire.quiz.repository.ScoreRepository;
import be.bewire.quiz.repository.entity.LeaderboardEntity;
import be.bewire.quiz.repository.entity.ScoreEntity;
import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class ScoreService {
    private final ScoreRepository scoreRepository;
    private final LeaderboardService leaderboardService;

    private final DozerBeanMapper mapper = new DozerBeanMapper();

    public ScoreService(ScoreRepository scoreRepository, @Lazy LeaderboardService leaderboardService) {

        this.scoreRepository = scoreRepository;
        this.leaderboardService = leaderboardService;
    }

    public void deleteScoresFromLeaderboard(LeaderboardEntity entity){
        this.scoreRepository.deleteScoreEntitiesByLeaderboardEntity(entity);
    }
    public void addNewEmptyUser(LeaderboardEntity leaderboardEntity, Long userId){
        Score score = new Score(userId, 0);
        ScoreEntity scoreEntity = mapper.map(score, ScoreEntity.class);
        scoreEntity.setLeaderboardEntity(leaderboardEntity);
        this.scoreRepository.save(scoreEntity);
        this.leaderboardService.saveLeaderboard(leaderboardEntity);
    }
    public void saveScore(ScoreEntity score){
        this.scoreRepository.save(score);
    }
}
