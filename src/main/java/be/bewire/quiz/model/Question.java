package be.bewire.quiz.model;

import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class Question {
    private String question;
    private Difficulty difficulty;
    private int timePerQuestion;
    private List<Answer> answers;
}
