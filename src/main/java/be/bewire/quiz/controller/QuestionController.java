package be.bewire.quiz.controller;

import be.bewire.quiz.model.Question;
import be.bewire.quiz.repository.QuizRepository;
import be.bewire.quiz.repository.entity.QuizEntity;
import lombok.AllArgsConstructor;
import org.dozer.DozerBeanMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/quiz")
@AllArgsConstructor
public class QuestionController {

    private final QuizRepository quizRepository;

    private final DozerBeanMapper mapper = new DozerBeanMapper();

    @DeleteMapping("/{quizId}/question/{questionId}")
    @Transactional
    public void deleteQuestion(@PathVariable String quizId, @PathVariable String questionId) {
        QuizEntity quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("QuizEntity for id " + quizId + " not found"));
        quiz.deleteQuestion(questionId);
        quizRepository.save(quiz);
    }

    @PatchMapping("/{quizId}/question/{questionId}")
    @Transactional
    public void updateQuestion(@PathVariable String quizId, @PathVariable String questionId, @RequestBody Question question) {
        QuizEntity quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("QuizEntity for id " + quizId + " not found"));
        quiz.updateQuestion(questionId, mapper.map(question, QuizEntity.Question.class));
        quizRepository.save(quiz);
    }

    @PatchMapping("/{quizId}/question")
    @Transactional
    public void addQuestionToQuiz(@PathVariable String quizId, @RequestBody Question question) {
        QuizEntity quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("QuizEntity for id " + quizId + " not found"));
        quiz.addQuestion(mapper.map(question, QuizEntity.Question.class));
        quizRepository.save(quiz);
    }
}
