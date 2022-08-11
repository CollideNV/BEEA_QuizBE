package be.bewire.quiz.controller;

import be.bewire.quiz.DTO.QuizDTO;
import be.bewire.quiz.DTO.QuizDetailDTO;
import be.bewire.quiz.repository.entity.QuizEntity;
import be.bewire.quiz.service.QuizService;
import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/quiz")
@Slf4j
public class QuizController {
    private final QuizService quizService;
    private final DozerBeanMapper mapper = new DozerBeanMapper();

    @Autowired
    public QuizController(QuizService quizService){
        this.quizService = quizService;
    }

    @GetMapping
    public List<QuizDTO> getAllQuizes(){
        List<QuizEntity> quizes = this.quizService.getAllQuizes();
        List<QuizDTO> quizesDTO = new ArrayList<QuizDTO>();
        for(QuizEntity quiz : quizes){
            QuizDTO quizDTO = mapper.map(quiz, QuizDTO.class);
            quizDTO.setCount(this.quizService.getAmountOfQuestions(quiz.getId()));
            quizesDTO.add(quizDTO);
        }
         return quizesDTO;
    }

    @GetMapping("/edit/{id}")
    public QuizEntity getExistingQuiz(@PathVariable String id) throws Exception {
        return this.quizService.getSpecificQuiz(id);
    }

    @PostMapping
    public QuizEntity makeQuiz(@RequestBody QuizDetailDTO quiz) throws Exception {
        return this.quizService.saveQuiz(quiz);
    }

    @PatchMapping("/{id}")
    public void updateQuiz(@PathVariable String id, @RequestBody QuizDetailDTO quizDetailDto) throws IOException {
        this.quizService.updateQuiz(id, quizDetailDto);
    }
    @DeleteMapping("/{id}")
    public void deleteQuiz(@PathVariable String id){
        try{
            this.quizService.deleteQuiz(id);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
    }
    @PostMapping("{id}/image")
    public void addImage(@PathVariable String id, @RequestParam MultipartFile file) throws IOException {
        this.quizService.addImage(id, file);
    }


}
