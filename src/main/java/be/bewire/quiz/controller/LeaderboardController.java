package be.bewire.quiz.controller;

import be.bewire.quiz.repository.entity.LeaderboardEntity;
import be.bewire.quiz.service.LeaderboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/leaderboard")
@Slf4j
public class LeaderboardController {
    private final LeaderboardService leaderboardService;

    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }
    @GetMapping("/{id}")
    public LeaderboardEntity getLeaderboard(@PathVariable String id) throws Exception {
        try{
            return this.leaderboardService.getLeaderboardForQuiz(id);
        }catch(Exception e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Quiz not found"
            );
        }
    }

}
