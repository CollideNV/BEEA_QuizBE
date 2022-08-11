package be.bewire.quiz.service;

import be.bewire.quiz.repository.AnswerRepository;
import be.bewire.quiz.repository.entity.AnswerEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AnswerServiceTest {
    @Mock
    AnswerRepository answerRepository;

    @InjectMocks
    AnswerService answerService;

    @Test
    void saveAnswer_Should_Save_Answer(){
        AnswerEntity answerEntity = new AnswerEntity();

        answerService.saveAnswer(answerEntity);

        verify(answerRepository, times(1)).save(answerEntity);
    }
}
