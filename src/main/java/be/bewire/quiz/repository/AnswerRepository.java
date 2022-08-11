package be.bewire.quiz.repository;

import be.bewire.quiz.repository.entity.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<AnswerEntity, String> {
    List<AnswerEntity> findAll();
    List<AnswerEntity> deleteAnswerEntitiesByQuestionEntityId(String id);
}
