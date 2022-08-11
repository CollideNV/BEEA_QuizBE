package be.bewire.quiz.repository.entity;

import be.bewire.quiz.model.Difficulty;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity(name = "question")
@NoArgsConstructor
@Getter
@Setter
public class QuestionEntity {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;
    @NotBlank(message = "Question is required")
    @Size(min = 1, max = 300)
    private String question;
    @NotBlank(message = "Difficulty is required")
    private Difficulty difficulty;
    @NotBlank(message = "Time is required")
    @Min(1)
    @Max(30)
    private int timePerQuestion;
    @JsonManagedReference
    @OneToMany(mappedBy = "questionEntity")
    @NotBlank(message = "Answer is required")
    private List<AnswerEntity> answers;
    @JsonBackReference
    @ManyToOne
    @NotBlank(message = "Quiz entity is required")
    private QuizEntity quizEntity;
}
