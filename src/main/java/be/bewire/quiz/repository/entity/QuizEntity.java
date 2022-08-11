package be.bewire.quiz.repository.entity;

import be.bewire.quiz.model.Difficulty;
import be.bewire.quiz.model.Quiz;
import be.bewire.quiz.model.TypeQuiz;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity(name = "quiz")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuizEntity extends AbstractAggregateRoot<QuizEntity> {
    @Id
    @GeneratedValue(generator = "system-uuid", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id = UUID.randomUUID().toString();
    @OneToOne(cascade = {CascadeType.ALL})
    @NotBlank(message = "Leaderboard is required")
    private LeaderboardEntity leaderBoard;
    @NotBlank(message = "Beginning time is required")
    private Date beginning;
    @NotBlank(message = "Ending time is required")
    private Date ending;
    @JsonManagedReference
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @NotNull
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


    public void addQuestion(QuestionEntity question) {
        questions.add(question);
        registerEvent(new QuestionAddedEvent(question, this));
    }

    public void deleteQuestion(String questionId) {
        questions.removeIf(questionEntity -> questionEntity.getId().equals(questionId));
        registerEvent(new QuestionDeletedEvent(questionId, this));
    }

    public void updateQuestion(String questionId, QuestionEntity question) {
        questions.removeIf(questionEntity -> questionEntity.getId().equals(questionId));
        questions.add(question);
        registerEvent(new QuestionUpdatedEvent(question, this));
    }

    @AllArgsConstructor
    @Data
    public static class QuestionDeletedEvent {
        private final String questionId;
        private final QuizEntity quiz;
    }

    @AllArgsConstructor
    @Data
    public static class QuestionUpdatedEvent {
        private final QuestionEntity question;
        private final QuizEntity quiz;
    }


    @AllArgsConstructor
    @Data
    public static class QuestionAddedEvent {
        private final QuestionEntity question;
        private final QuizEntity quiz;
    }

    public void recalculateDifficulty() {
        if (questions.size() == 0) {
            this.difficulty = Difficulty.EASY;
            return;
        }
        long easyOccurences = this.questions.stream().filter(q -> q.getDifficulty().equals(Difficulty.EASY)).count();
        double mediumOccurences = (this.questions.stream().filter(q -> q.getDifficulty().equals(Difficulty.MEDIUM)).count()) * 2;
        long hardOccurences = (this.questions.stream().filter(q -> q.getDifficulty().equals(Difficulty.HARD)).count()) * 3;

        double calculatedDifficulty = (easyOccurences + mediumOccurences + hardOccurences) / questions.size();

        if (calculatedDifficulty < 1.75) {
            this.difficulty = Difficulty.EASY;
        } else if (calculatedDifficulty < 2.25) {
            this.difficulty = Difficulty.MEDIUM;
        } else {
            this.difficulty = Difficulty.HARD;
        }
    }
}
