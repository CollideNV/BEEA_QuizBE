package be.bewire.quiz.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
public class Question {
    private String id = UUID.randomUUID().toString();
    @NonNull
    private String question;
    @NonNull
    private Difficulty difficulty;
    @NonNull
    private int timePerQuestion;
    private List<Answer> answers = new ArrayList<>();
}
