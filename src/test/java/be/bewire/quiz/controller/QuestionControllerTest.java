package be.bewire.quiz.controller;


import be.bewire.quiz.DTO.QuizDetailDTO;
import be.bewire.quiz.model.Question;
import be.bewire.quiz.repository.entity.QuestionEntity;
import be.bewire.quiz.repository.entity.QuizEntity;
import be.bewire.quiz.service.QuestionService;
import be.bewire.quiz.service.QuizService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@ExtendWith(SpringExtension.class)
@WebMvcTest(QuestionController.class)
public class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionService questionService;

    @InjectMocks
    QuizService quizService;

    @BeforeEach
    void init(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void deleteQuestion_Should_Remove_Question(){
        QuestionEntity questionEntity = new QuestionEntity();
        questionService.saveQuestion(questionEntity);

        try{
            mockMvc.perform(MockMvcRequestBuilders.delete("/question/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("utf-8"))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Test
    void addQuestionToQuiz_Should_Add_Question(){
        QuizDetailDTO quiz = new QuizDetailDTO();
        Question question = new Question();
        try{
            QuizEntity entity = quizService.saveQuiz(quiz);

            mockMvc.perform(MockMvcRequestBuilders.post("/question/"+entity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.toJson(question))
                    .characterEncoding("utf-8"))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
