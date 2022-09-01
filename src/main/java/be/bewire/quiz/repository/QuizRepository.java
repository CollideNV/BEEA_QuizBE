package be.bewire.quiz.repository;

import be.bewire.quiz.repository.entity.QuizEntity;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface QuizRepository extends
        CrudRepository<QuizEntity, String> {
}
