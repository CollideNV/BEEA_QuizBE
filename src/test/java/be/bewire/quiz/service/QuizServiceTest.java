package be.bewire.quiz.service;

import be.bewire.quiz.DTO.QuizDetailDTO;
import be.bewire.quiz.exception.BadRequestException;
import be.bewire.quiz.model.Answer;
import be.bewire.quiz.model.Difficulty;
import be.bewire.quiz.model.Question;
import be.bewire.quiz.model.TypeQuiz;
import be.bewire.quiz.repository.QuizRepository;
import be.bewire.quiz.repository.entity.QuestionEntity;
import be.bewire.quiz.repository.entity.QuizEntity;
import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuizServiceTest {
    private final DozerBeanMapper mapper = new DozerBeanMapper();
    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuestionService questionService;

    @Mock
    private AnswerService answerService;

    @Mock
    private LeaderboardService leaderboardService;

    @Mock
    private ScoreService scoreService;

    @InjectMocks
    private QuizService quizService;

    @Test
    public void saveQuiz_Correctly_creates_Quiz() throws Exception {
        QuizDetailDTO quiz = new QuizDetailDTO();
        Date date = new Date();
        quiz.setTheme("music");
        quiz.setTitle("music");
        quiz.setType(TypeQuiz.regular);
        quiz.setBeginning(new Date(System.currentTimeMillis() - 3600 * 1000));
        quiz.setEnding(new Date(System.currentTimeMillis() + 3600 * 1000));

        List<Answer> answers = new ArrayList<Answer>();
        Answer answer = new Answer();
        answers.add(answer);

        List<Question> questions = new ArrayList<Question>();
        Question question = new Question();
        question.setAnswers(answers);
        question.setDifficulty(Difficulty.hard);
        questions.add(question);

        QuizEntity toSaveEntity = mapper.map(quiz, QuizEntity.class);
        QuizEntity toReturnEntity = toSaveEntity;

        when(quizRepository.save(Mockito.any(QuizEntity.class))).thenReturn(toReturnEntity);

        QuizEntity result = quizService.saveQuiz(quiz);

        assertEquals(toReturnEntity, result);
    }

    @Test
    public void getAmountOfQuestions_Should_Return_Correct_Amount(){
        QuizEntity entity = new QuizEntity();
        List<QuestionEntity> questions = new ArrayList<QuestionEntity>();
        QuestionEntity question = new QuestionEntity();
        questions.add(question);
        entity.setQuestions(questions);

        when(this.quizRepository.getById("a")).thenReturn(entity);

        assertEquals(1, quizService.getAmountOfQuestions("a"));
    }

    @Test
    public void getSpecificQuiz_Should_Return_QuizEntity() throws Exception {
        QuizEntity entity = new QuizEntity();
        when(quizRepository.findById(anyString())).thenReturn(Optional.of(entity));

        QuizEntity savedEntity = quizService.getSpecificQuiz("1");

        assertEquals(savedEntity, entity);
        verify(quizRepository, times(1)).findById((anyString()));
    }

    @Test
    public void getSpecificQuiz_Should_Throw_Exception_When_Not_Found(){
        when(quizRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(Exception.class, () ->{
            QuizEntity entity = quizService.getSpecificQuiz("A");
            assertNull(entity);
        });
        verify(quizRepository, times(1)).findById((anyString()));
    }

    @Test
    public void deleteQuiz_Should_Throw_Exception_When_Not_Found(){
        when(quizRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(Exception.class, () ->{
            quizService.deleteQuiz("A");
        });

        verify(quizRepository, times(1)).findById((anyString()));
    }

    @Test
    public void deleteQuiz_Should_Delete_Quiz() throws Exception {
        QuizEntity entity = new QuizEntity();
        when(quizRepository.findById(anyString())).thenReturn(Optional.of(entity));
        quizService.deleteQuiz("A");

        verify(quizRepository, times(1)).delete((entity));
    }

    @Test
    public void updateDifficulty_Should_Update_Difficulty_To_Hard() throws Exception {
        QuestionEntity q1 = new QuestionEntity();
        QuestionEntity q2 = new QuestionEntity();
        QuizEntity entity = new QuizEntity();
        List<QuestionEntity> list = new ArrayList<QuestionEntity>();
        q1.setDifficulty(Difficulty.hard);
        q2.setDifficulty(Difficulty.hard);
        list.add(q1);
        list.add(q2);
        entity.setQuestions(list);
        quizService.updateDifficulty(entity);

        assertEquals(entity.getDifficulty(), Difficulty.hard);
    }

    @Test
    public void addImage_Should_Throw_BadRequestException_When_File_Null(){
        assertThrows(BadRequestException.class, () ->{
            quizService.addImage("1", null);
        });
    }

    @Test
    public void updateQuiz_Should_Throw_BadRequestException_When_Dates_Wrong(){
        QuizDetailDTO quiz = new QuizDetailDTO();
        quiz.setEnding(new Date(1,2,3,4,5,6));

        assertThrows(BadRequestException.class, () ->{
           quizService.updateQuiz("1", quiz);
        });
    }

    @Test
    public void updateQuiz_Should_Throw_BadRequestException_When_Fields_Not_Filled(){
        QuizDetailDTO quiz = new QuizDetailDTO();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, 1);
        Date begin = cal.getTime();
        cal.add(Calendar.HOUR_OF_DAY, 1);
        Date end = cal.getTime();
        quiz.setBeginning(begin);
        quiz.setEnding(end);

        assertThrows(BadRequestException.class, () ->{
            quizService.updateQuiz("1", quiz);
        });
    }

    @Test
    public void updateQuiz_Should_Update_Quiz(){
        QuizDetailDTO quiz = new QuizDetailDTO();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, 1);
        Date begin = cal.getTime();
        cal.add(Calendar.HOUR_OF_DAY, 1);
        Date end = cal.getTime();
        quiz.setBeginning(begin);
        quiz.setEnding(end);
        quiz.setType(TypeQuiz.regular);
        quiz.setTitle("a");
        quiz.setTheme("a");

        QuizEntity entity = new QuizEntity();
        when(quizRepository.getById(anyString())).thenReturn(mapper.map(quiz, QuizEntity.class));
        quizService.updateQuiz("a", quiz);

        verify(quizRepository, times(1)).getById("a");

    }
}
