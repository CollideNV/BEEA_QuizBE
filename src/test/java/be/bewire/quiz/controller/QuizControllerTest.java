package be.bewire.quiz.controller;


import be.bewire.quiz.model.Difficulty;
import be.bewire.quiz.model.Question;
import be.bewire.quiz.model.Quiz;
import be.bewire.quiz.model.TypeQuiz;
import be.bewire.quiz.repository.entity.QuizEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static be.bewire.quiz.controller.JsonUtil.toJson;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@RecordApplicationEvents
public class QuizControllerTest {
    @Autowired
    private ApplicationEvents applicationEvents;
    @Autowired
    private MockMvc mvc;

    @DisplayName("Create a basic quiz. Afterwards and and delete a question")
    @Test
    void createQuizAndAddUpdateAndDeleteAQuestion() throws Exception {
        Quiz quiz = new Quiz(new Date(), new Date(), TypeQuiz.LIVE, "theme", "Title");

        // Add Quiz
        MvcResult mvcResult = mvc.perform(post("/quiz")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(quiz))
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();

        Quiz returnedQuiz = new ObjectMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd")).readValue(mvcResult.getResponse().getContentAsString(), Quiz.class);

        // Add Question
        mvc.perform(patch("/quiz/" + returnedQuiz.getId() + "/question")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(new Question("Is dit een vraag?", Difficulty.EASY, 30))))
                .andExpect(status().isOk());

        List<QuizEntity.QuestionAddedEvent> questionAddedEvents = applicationEvents
                .stream(QuizEntity.QuestionAddedEvent.class)
                .filter(event -> event.getQuestion().getQuestion().equals("Is dit een vraag?")).collect(Collectors.toList());

        assertEquals(1, questionAddedEvents.size());
        var addedQuestionId = questionAddedEvents.get(0).getQuestion().getId();

        // Check if Question is Returned
        mvc.perform(get("/quiz/" + returnedQuiz.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questions", hasSize(1)))
                .andExpect(jsonPath("$.questions[0].question", is("Is dit een vraag?")))
                .andExpect(jsonPath("$.difficulty", is("EASY")))
                .andReturn();

        // Update Question
        mvc.perform(patch("/quiz/" + returnedQuiz.getId() + "/question/" + addedQuestionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(new Question(addedQuestionId, "Of is dit de vraag?", Difficulty.HARD, 30, Collections.emptyList()))))
                .andExpect(status().isOk());


        // Check if Updated Question is Returned
        mvc.perform(get("/quiz/" + returnedQuiz.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questions", hasSize(1)))
                .andExpect(jsonPath("$.questions[0].question", is("Of is dit de vraag?")))
                .andExpect(jsonPath("$.difficulty", is("HARD")))
                .andReturn();

        // Delete Question
        mvc.perform(delete("/quiz/" + returnedQuiz.getId() + "/question/" + addedQuestionId))
                .andExpect(status().isOk());

        // Check if Question is deleted
        mvc.perform(get("/quiz/" + returnedQuiz.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.difficulty", is("EASY")))
                .andExpect(jsonPath("$.questions", hasSize(0)));


        assertEquals(1, applicationEvents
                .stream(QuizEntity.QuestionDeletedEvent.class)
                .count());

    }
}
