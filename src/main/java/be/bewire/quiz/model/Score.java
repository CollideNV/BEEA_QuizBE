package be.bewire.quiz.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class Score {

    private Long userId;
    private int score;


}
