package be.bewire.quiz.controller;

import be.bewire.quiz.model.Quiz;
import be.bewire.quiz.repository.QuizRepository;
import be.bewire.quiz.repository.entity.QuizEntity;
import be.bewire.quiz.service.FirebaseFileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/quiz")
@Slf4j
@AllArgsConstructor
public class QuizController {

    private QuizRepository quizRepository;
    private final DozerBeanMapper mapper = new DozerBeanMapper();
    private FirebaseFileService firebaseFileService;


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

    @PostMapping("{id}/image")
    public void addImage(@PathVariable String id, @RequestParam @NotNull MultipartFile file) throws IOException {
        QuizEntity quiz = quizRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Can't find quiz with id " + id));
        quiz.setQuizImage(this.firebaseFileService.saveImage(file, id));
        this.quizRepository.save(quiz);
    }

    // @PostMapping
    public void startGame() {
        // Whatever we need to do to start a game for this quiz.
        // The Idea is that this could also work with a scheduler.
        // It will probably call an endpoint.
    }
}