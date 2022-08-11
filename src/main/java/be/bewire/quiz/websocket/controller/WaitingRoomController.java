package be.bewire.quiz.websocket.controller;

import be.bewire.quiz.repository.entity.LeaderboardEntity;
import be.bewire.quiz.service.LeaderboardService;
import be.bewire.quiz.websocket.dto.LeaderboardEntryDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WaitingRoomController {
    private final LeaderboardService leaderboardService;
    final SimpMessagingTemplate simpMessagingTemplate;

    public WaitingRoomController(LeaderboardService leaderboardService, SimpMessagingTemplate simpMessagingTemplate){
        this.leaderboardService = leaderboardService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/waitingroom.adduser")
    public void addLiveToLeaderboard(@Payload LeaderboardEntryDto leaderboardEntryDto) throws Exception {
        System.out.println("ello");
        this.leaderboardService.addUserToLeaderboard(leaderboardEntryDto.getId(), leaderboardEntryDto.getUserId());
        LeaderboardEntity entity = leaderboardService.getLeaderboard(leaderboardEntryDto.getId());
        simpMessagingTemplate.convertAndSend("/topic/adduser", entity);
    }
}
