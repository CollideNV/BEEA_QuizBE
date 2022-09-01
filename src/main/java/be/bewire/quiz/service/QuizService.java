package be.bewire.quiz.service;

import be.bewire.quiz.repository.entity.QuizEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@Transactional
@Slf4j
public class QuizService {


    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleCustom(QuizEntity.QuestionAddedEvent event) {
        event.getQuiz().recalculateDifficulty();
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleCustom(QuizEntity.QuestionDeletedEvent event) {
        event.getQuiz().recalculateDifficulty();
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleCustom(QuizEntity.QuestionUpdatedEvent event) {
        event.getQuiz().recalculateDifficulty();
    }
}
