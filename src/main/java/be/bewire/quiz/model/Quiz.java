package be.bewire.quiz.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@RequiredArgsConstructor
public class Quiz {
    private String id;

    @NonNull
    private Date beginning;
    @NonNull
    private Date ending;
    private List<Question> questions = new ArrayList<>();
    @NonNull private TypeQuiz type;
    @NonNull private String theme;
    private Difficulty difficulty = Difficulty.EASY;
    @NonNull private String title;
    private MultipartFile quizImage;



    public void UpdateImage(MultipartFile newImage){
        this.quizImage = newImage;
    }



}
