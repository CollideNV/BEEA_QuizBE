package be.bewire.quiz.controller;

import be.bewire.quiz.DTO.QuizAnswerDTO;
import be.bewire.quiz.service.GameService;
import be.bewire.quiz.service.LeaderboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/game")
@Slf4j
public class GameController {
    private final LeaderboardService leaderboardService;
    private final GameService gameService;

    public GameController(LeaderboardService leaderboardService, GameService gameService) {
        this.leaderboardService = leaderboardService;
        this.gameService = gameService;
    }

    @PostMapping("/{id}")
    public boolean addToLeaderboard(@PathVariable String id, @RequestBody Long userId) {
        try{
            return this.leaderboardService.addUserToLeaderboard(id, userId);
        }catch(Exception e){
            return true;
        }
    }

    @PostMapping("/answer")
    public void answerQuestion(@RequestBody QuizAnswerDTO answer) throws Exception {
        this.gameService.answerQuestion(answer);
    }



}
