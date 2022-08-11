package be.bewire.quiz.model;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class Answer {
    private boolean correct;
    private String answer;
}
