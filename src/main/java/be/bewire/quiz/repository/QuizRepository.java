package be.bewire.quiz.repository;



import be.bewire.quiz.repository.entity.QuizEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<QuizEntity, String>{
}
