package be.collide.quizbackend.domain;

import java.util.List;
import java.util.UUID;

public interface QuizAnswers {

    List<QuizAnswer> findForQuestion(UUID quizId, UUID questionId);

    void addQuizAnswer(QuizAnswer quizAnswer);
}
