package be.bewire.quiz.repository.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity(name = "answer")
@NoArgsConstructor
@Getter
@Setter
public class AnswerEntity {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;
    @NotBlank(message = "Corectness is required")
    private boolean correct;
    @NotBlank(message = "Answer is required")
    @Size(min = 1, max = 100)
    private String answer;
    @JsonBackReference
    @ManyToOne
    @NotBlank(message = "Question entity is required")
    private QuestionEntity questionEntity;

    public AnswerEntity(boolean correct, String answer) {
        this.correct = correct;
        this.answer = answer;
    }
}
