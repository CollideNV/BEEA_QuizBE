package be.bewire.quiz.service;

import be.bewire.quiz.DTO.QuizAnswerDTO;
import be.bewire.quiz.repository.entity.LeaderboardEntity;
import be.bewire.quiz.repository.entity.QuizEntity;
import be.bewire.quiz.repository.entity.ScoreEntity;
import jakarta.ws.rs.BadRequestException;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private final QuizService quizService;
    private final ScoreService scoreService;

    public GameService(QuizService quizService, ScoreService scoreService){
        this.scoreService = scoreService;
        this.quizService = quizService;
    }

    public void answerQuestion(QuizAnswerDTO answer) throws Exception {
        if(answer.getQuizId() == null || answer.getQuizId().equals("")) throw new BadRequestException("No quizId provided");

        QuizEntity quiz = quizService.getSpecificQuiz(answer.getQuizId());
        LeaderboardEntity leaderboard = quiz.getLeaderBoard();
        for(ScoreEntity scoreEntity : leaderboard.getScores()){
            if(scoreEntity.getUserId() == answer.getUserId()){
                int scorePreAnswer = scoreEntity.getScore();
                scoreEntity.setScore(scorePreAnswer + answer.getScore());
                this.scoreService.saveScore(scoreEntity);
            }
        }
    }
}
