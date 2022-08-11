package be.bewire.quiz.repository;

import be.bewire.quiz.repository.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<QuestionEntity, String> {
    void deleteAllByQuizEntity_Id(String id);
    List<QuestionEntity> findAllByQuizEntity_Id(String id);
}
