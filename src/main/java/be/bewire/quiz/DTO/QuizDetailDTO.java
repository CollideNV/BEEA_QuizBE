package be.bewire.quiz.DTO;

import be.bewire.quiz.model.TypeQuiz;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuizDetailDTO {
    private Date beginning;
    private Date ending;
    private TypeQuiz type;
    private String theme;
    private String title;
}
