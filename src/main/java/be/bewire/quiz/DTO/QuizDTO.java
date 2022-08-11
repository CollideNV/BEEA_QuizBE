package be.bewire.quiz.DTO;

import be.bewire.quiz.repository.entity.QuizEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuizDTO {
    private String id;
    private Date beginning;
    private Date ending;
    private String type;
    private String difficulty;
    private String title;
    private int count;
    private String quizImage;

    public QuizDTO(QuizEntity entity){}
}
