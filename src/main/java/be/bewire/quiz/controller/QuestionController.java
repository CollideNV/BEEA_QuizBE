package be.bewire.quiz.controller;

import be.bewire.quiz.model.Question;
import be.bewire.quiz.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/question")
@Slf4j
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable String id){
        try{
            this.questionService.deleteQuestion(id);
        }catch(Exception e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
    }

    @PatchMapping("/{id}")
    public void updateQuestion(@PathVariable String id, @RequestBody Question question) throws Exception {
        this.questionService.updateQuestion(id, question);
    }

    @PostMapping("/{id}")
    public String addQuestionToQuiz(@PathVariable String id, @RequestBody Question question) throws Exception {
        return this.questionService.addQuestionToQuiz(id, question);
    }

}
