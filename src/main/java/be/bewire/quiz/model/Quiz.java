package be.bewire.quiz.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class Quiz {
    private Leaderboard leaderBoard;
    private Date beginning;
    private Date ending;
    private List<Question> questions;
    private TypeQuiz type;
    private String theme;
    private Difficulty difficulty;
    private String title;
    private MultipartFile quizImage;

    public Quiz(Date beginning, Date ending, List<Question> questions, TypeQuiz type, String title, MultipartFile quizImage, Leaderboard leaderboard, String theme) {
        this.beginning = beginning;
        this.ending = ending;
        this.questions = questions;
        this.type = type;
        this.title = title;
        this.quizImage = quizImage;
        this.leaderBoard = leaderboard;
        this.setDifficulty();
        this.theme = theme;
    }

    public void UpdateImage(MultipartFile newImage){
        this.quizImage = newImage;
    }

    public void UpdateLeaderboard(Leaderboard newLeaderBoard){
        this.leaderBoard = newLeaderBoard;
    }

    public void setDifficulty(){
        long easyOccurences = this.questions.stream().filter(q -> q.getDifficulty().equals(Difficulty.easy)).count();
        double mediumOccurences = (this.questions.stream().filter(q -> q.getDifficulty().equals(Difficulty.medium)).count()) * 2;
        long hardOccurences = (this.questions.stream().filter(q -> q.getDifficulty().equals(Difficulty.hard)).count()) * 3;

        double calculatedDifficulty = (easyOccurences + mediumOccurences + hardOccurences) / questions.size();

        if(calculatedDifficulty < 1.75){
            this.difficulty = Difficulty.easy;
        }else if(calculatedDifficulty < 2.25){
            this.difficulty = Difficulty.medium;
        }else{
            this.difficulty = Difficulty.hard;
        }
    }
}
