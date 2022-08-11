package be.bewire.quiz.service;

import be.bewire.quiz.model.Answer;
import be.bewire.quiz.repository.AnswerRepository;
import be.bewire.quiz.repository.entity.AnswerEntity;
import be.bewire.quiz.repository.entity.QuestionEntity;
import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class AnswerService {
    private final AnswerRepository answerRepository;

    private final DozerBeanMapper mapper = new DozerBeanMapper();

    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public void saveAnswer(AnswerEntity answer){
        this.answerRepository.save(answer);
    }

    public void deleteAnswersFromQuestion(String id){
        this.answerRepository.deleteAnswerEntitiesByQuestionEntityId(id);
    }
    public void addAnswersForQuestion(List<Answer> answers, QuestionEntity questionEntity){
        answers.forEach(answer -> {
            AnswerEntity answerEntity = mapper.map(answer, AnswerEntity.class);
            answerEntity.setQuestionEntity(questionEntity);
            this.saveAnswer(answerEntity);
        });
    }
}
