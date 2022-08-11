package be.bewire.quiz.model;

import be.bewire.quiz.repository.entity.LeaderboardEntity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class Leaderboard {
    private List<Score> scores;

    public Leaderboard(LeaderboardEntity entity){}
}
