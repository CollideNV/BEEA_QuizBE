package be.bewire.quiz.repository.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity(name = "leaderboard")
@NoArgsConstructor
@Getter
@Setter
public class LeaderboardEntity {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;
    @JsonManagedReference
    @OneToMany(mappedBy = "leaderboardEntity")
    @NotBlank(message = "Scores are required")
    private List<ScoreEntity> scores;

    public LeaderboardEntity(List<ScoreEntity> scores) {
        this.scores = scores;
    }
}
