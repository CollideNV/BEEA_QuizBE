package be.bewire.quiz.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuizAnswerDTO {
    private int userId;
    private String quizId;
    private int score;
}
