package be.bewire.quiz.repository.entity;

import be.bewire.quiz.model.Difficulty;
import be.bewire.quiz.model.Quiz;
import be.bewire.quiz.model.TypeQuiz;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity(name = "quiz")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuizEntity {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;
    @OneToOne
    @NotBlank(message = "Leaderboard is required")
    private LeaderboardEntity leaderBoard;
    @NotBlank(message = "Beginning time is required")
    private Date beginning;
    @NotBlank(message = "Ending time is required")
    private Date ending;
    @JsonManagedReference
    @OneToMany(mappedBy = "quizEntity")
    @NotBlank(message = "Questions are required")
    private List<QuestionEntity> questions;
    @NotBlank(message = "Type is required")
    private TypeQuiz type;
    @NotBlank(message = "Theme is required")
    private String theme;
    @NotBlank(message = "Difficulty is required")
    private Difficulty difficulty;
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 200)
    private String title;
    @Column(length = 1000)
    private String quizImage;

    public QuizEntity(Quiz quiz) {
    }
}
