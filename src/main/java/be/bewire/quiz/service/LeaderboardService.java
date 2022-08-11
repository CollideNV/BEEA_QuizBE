package be.bewire.quiz.service;

import be.bewire.quiz.repository.LeaderboardRepository;
import be.bewire.quiz.repository.entity.LeaderboardEntity;
import be.bewire.quiz.repository.entity.QuizEntity;
import be.bewire.quiz.repository.entity.ScoreEntity;
import jakarta.ws.rs.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class LeaderboardService {

    private final LeaderboardRepository leaderboardRepository;
    private final ScoreService scoreService;

    private final QuizService quizService;

    public LeaderboardService(LeaderboardRepository leaderboardRepository, @Lazy ScoreService scoreService, @Lazy QuizService quizService){
        this.leaderboardRepository = leaderboardRepository;
        this.scoreService = scoreService;
        this.quizService = quizService;
    }

    public void saveLeaderboard(LeaderboardEntity leaderboard){
        this.leaderboardRepository.save(leaderboard);
    }

    public void deleteLeaderboard(LeaderboardEntity entity){this.leaderboardRepository.delete(entity);}

    public boolean addUserToLeaderboard(String id, Long userId) throws Exception {
        if(id == null || id.equals("")) throw new BadRequestException("Id not present");
        if(userId == null) throw new BadRequestException("userId has to be present");

        QuizEntity quiz = this.quizService.getSpecificQuiz(id);
        LeaderboardEntity leaderboard = this.leaderboardRepository.getById(quiz.getLeaderBoard().getId());
        for(ScoreEntity score : leaderboard.getScores()){
            if(score.getUserId() == userId){
                throw new Exception("This user already exists in the leaderboard");
            }
        }
        this.scoreService.addNewEmptyUser(leaderboard, userId);
        return false;
    }
    public LeaderboardEntity getLeaderboard(String id) throws Exception {
        LeaderboardEntity leaderboard = this.leaderboardRepository.getById(id);
        if(leaderboard == null){
            throw new Exception("Leaderboard not found");
        }
        return leaderboard;
    }
    public LeaderboardEntity getLeaderboardForQuiz(String id) throws Exception {
        QuizEntity quiz = this.quizService.getSpecificQuiz(id);
        if(quiz != null){
            LeaderboardEntity leaderboard = quiz.getLeaderBoard();
            return leaderboard;
        }else{
            throw new Exception("Quiz not found");
        }

    }
}
