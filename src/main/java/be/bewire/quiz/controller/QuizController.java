package be.bewire.quiz.controller;

import be.bewire.quiz.model.Quiz;
import be.bewire.quiz.repository.QuizRepository;
import be.bewire.quiz.repository.entity.QuizEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/quiz")
@Slf4j
@AllArgsConstructor
public class QuizController {
    private QuizRepository quizRepository;
    private final DozerBeanMapper mapper = new DozerBeanMapper();


    @PostMapping
    public Quiz makeQuiz(@RequestBody Quiz quiz) {
        QuizEntity quizEntity = mapper.map(quiz, QuizEntity.class);
        return mapper.map(quizRepository.save(quizEntity), Quiz.class);
    }

    @GetMapping("/{id}")
    public Quiz getExistingQuiz(@PathVariable String id) {
        QuizEntity quiz = quizRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Can't find quiz with id " + id));
        return mapper.map(quiz, Quiz.class);
    }

}
