package be.bewire.quiz.service;

import be.bewire.quiz.repository.QuestionRepository;
import be.bewire.quiz.repository.entity.QuestionEntity;
import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {
    private final DozerBeanMapper mapper = new DozerBeanMapper();
    @Mock
    QuestionRepository questionRepository;

    @Mock
    QuizService quizService;

    @Mock
    AnswerService answerService;

    @InjectMocks
    QuestionService questionService;



    @Test
    public void saveQuestion_Should_Save_Question_Correctly(){
        QuestionEntity question = new QuestionEntity();

        questionService.saveQuestion(question);

        verify(questionRepository, times(1)).save(question);
    }
}
