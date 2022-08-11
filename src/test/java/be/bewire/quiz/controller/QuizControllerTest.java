package be.bewire.quiz.controller;

import be.bewire.quiz.DTO.QuizDetailDTO;
import be.bewire.quiz.model.Quiz;
import be.bewire.quiz.repository.entity.QuizEntity;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(QuizController.class)
public class QuizControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private be.bewire.quiz.service.QuizService QuizService;

    @InjectMocks
    QuizController quizController;

    @BeforeEach
    void init(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void getAllQuizes_Should_Return_QuizDTO_Object_List(){
        List<QuizEntity> toReturn = new ArrayList<QuizEntity>();
        when(QuizService.getAllQuizes()).thenReturn(toReturn);
        try{
            mockMvc.perform(MockMvcRequestBuilders.get("/quiz")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("utf-8"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(JsonUtil.toJson(toReturn)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    void makeQuiz_Should_Return_OK(){
        Quiz quiz = new Quiz();
        try{
            mockMvc.perform(MockMvcRequestBuilders.post("/quiz")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.toJson(quiz))
                    .characterEncoding("utf-8"))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    void getExistingQuiz_Should_Return_OK_And_Quiz() throws Exception {
        QuizEntity toReturn = new QuizEntity();
        when(QuizService.getSpecificQuiz("1")).thenReturn(toReturn);
        try{
            mockMvc.perform(MockMvcRequestBuilders.get("/quiz/edit/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("utf-8"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(JsonUtil.toJson(toReturn)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    void updateQuiz_Should_Return_OK(){
        QuizDetailDTO quiz = new QuizDetailDTO();

        try{
            QuizEntity quizEntity = QuizService.saveQuiz(quiz);
            mockMvc.perform(MockMvcRequestBuilders.patch("/quiz/"+ quizEntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.toJson(quiz))
                    .characterEncoding("utf-8"))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
