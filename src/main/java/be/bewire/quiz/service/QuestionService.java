package be.bewire.quiz.service;

import be.bewire.quiz.model.Question;
import be.bewire.quiz.repository.QuestionRepository;
import be.bewire.quiz.repository.entity.AnswerEntity;
import be.bewire.quiz.repository.entity.QuestionEntity;
import be.bewire.quiz.repository.entity.QuizEntity;
import jakarta.ws.rs.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final AnswerService answerService;
    private final QuizService quizService;

    private final DozerBeanMapper mapper = new DozerBeanMapper();

    public QuestionService(QuestionRepository questionRepository, AnswerService answerService, @Lazy QuizService quizService) {
        this.questionRepository = questionRepository;
        this.answerService = answerService;
        this.quizService = quizService;
    }
    public QuestionEntity saveQuestion(QuestionEntity question){
        return this.questionRepository.save(question);
    }

    public void deleteQuestionsForQuiz(String id){
        for(QuestionEntity entity : this.questionRepository.findAllByQuizEntity_Id(id)){
            this.answerService.deleteAnswersFromQuestion(entity.getId());
        }
        this.questionRepository.deleteAllByQuizEntity_Id(id);
    }

    public void deleteQuestion(String id) throws Exception {
        if(this.questionRepository.findById(id).get() == null){
            throw new Exception("No such question in DB");
        }
        this.answerService.deleteAnswersFromQuestion(id);
        this.questionRepository.deleteById(id);
    }

    public String addQuestionToQuiz(String id, Question question) throws Exception {
        if (question.getAnswers() == null || question.getQuestion() == null || question.getAnswers().size() < 4) throw new BadRequestException("Not all fields for Question provided");
        if (question.getTimePerQuestion() <= 0) question.setTimePerQuestion(20);

        QuizEntity quiz = this.quizService.getSpecificQuiz(id);
        QuestionEntity questionEntity = mapper.map(question, QuestionEntity.class);
        questionEntity.setQuizEntity(quiz);
        QuestionEntity savedQuestion = this.questionRepository.save(questionEntity);
        for(AnswerEntity answer : questionEntity.getAnswers()){
            answer.setQuestionEntity(questionEntity);
            this.answerService.saveAnswer(answer);
        }
        this.quizService.updateDifficulty(quiz);
        return savedQuestion.getId();
    }
    public void updateQuestion(String id, Question question) throws Exception {
        if (question.getAnswers() == null || question.getQuestion() == null || question.getAnswers().size() < 4 || question.getQuestion().equals("")) throw new BadRequestException("Not all fields for Question provided");
        if (question.getTimePerQuestion() == 0) question.setTimePerQuestion(20);

        QuestionEntity updateEntity = this.questionRepository.getById(id);
        updateEntity.setQuestion(question.getQuestion());
        updateEntity.setDifficulty(question.getDifficulty());
        updateEntity.setTimePerQuestion(question.getTimePerQuestion());
        int count = 0;
        for(AnswerEntity answerEntity : updateEntity.getAnswers()){
            answerEntity.setAnswer(question.getAnswers().get(count).getAnswer());
            answerEntity.setCorrect(question.getAnswers().get(count).isCorrect());
            answerService.saveAnswer(answerEntity);
            count++;
        }
        this.questionRepository.save(updateEntity);
        this.quizService.updateDifficulty(updateEntity.getQuizEntity());
    }
    public List<QuestionEntity> addQuestionsForQuiz(List<Question> questions, QuizEntity quizEntity){
        List<QuestionEntity> questionEntities =  new ArrayList<QuestionEntity>();
        questions.forEach(question -> {
            QuestionEntity questionEntity = mapper.map(question, QuestionEntity.class);
            questionEntity.setQuizEntity(quizEntity);
            questionEntities.add(questionEntity);
            this.saveQuestion(questionEntity);
            this.answerService.addAnswersForQuestion(question.getAnswers(), questionEntity);
        });
        return questionEntities;
    }
    public List<QuestionEntity> getQuestionsForQuiz(QuizEntity entity) throws Exception {
        List<QuestionEntity> list = this.questionRepository.findAllByQuizEntity_Id(entity.getId());
        if(list != null){
            return list;
        }else{
            throw new Exception("No such quiz found");
        }
    }
    public QuestionEntity getQuestion(String id) throws Exception{
        QuestionEntity question = questionRepository.getById(id);
        if(question == null){
            throw new Exception("Question does noet exist");
        }
        return question;
    }
}
