package be.bewire.quiz.service;

import be.bewire.quiz.repository.LeaderboardRepository;
import be.bewire.quiz.repository.entity.LeaderboardEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LeaderboardServiceTest {
    @Mock
    LeaderboardRepository leaderboardRepository;

    @InjectMocks
    LeaderboardService leaderboardService;

    @Test
    void saveLeaderboard_Should_Save_Leaderboard(){
        LeaderboardEntity leaderboardEntity = new LeaderboardEntity();

        this.leaderboardService.saveLeaderboard(leaderboardEntity);

        verify(leaderboardRepository, times(1)).save(leaderboardEntity);
    }
}
