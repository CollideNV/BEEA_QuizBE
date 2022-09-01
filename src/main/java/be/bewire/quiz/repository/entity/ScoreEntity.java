package be.bewire.quiz.repository.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity(name = "score")
@NoArgsConstructor
@Getter
@Setter
public class ScoreEntity {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;
    @Column(name = "userid")
    @NotBlank(message = "User id is required")
    private Long userId;
    @NotBlank(message = "Score is required")
    private int score;
    @JsonBackReference
    @ManyToOne
    @NotBlank(message = "Leaderboard entity is required")
    private LeaderboardEntity leaderboardEntity;

}
