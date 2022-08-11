package be.bewire.quiz.service;

import be.bewire.quiz.DTO.QuizDetailDTO;
import be.bewire.quiz.exception.BadRequestException;
import be.bewire.quiz.model.Difficulty;
import be.bewire.quiz.model.Leaderboard;
import be.bewire.quiz.repository.QuizRepository;
import be.bewire.quiz.repository.entity.LeaderboardEntity;
import be.bewire.quiz.repository.entity.QuestionEntity;
import be.bewire.quiz.repository.entity.QuizEntity;
import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@Transactional
@Slf4j
public class QuizService {
    private final QuizRepository quizRepository;
    private final LeaderboardService leaderboardService;
    private final QuestionService questionService;
    private final ScoreService scoreService;
    private final FirebaseFileService firebaseFileService;

    private final DozerBeanMapper mapper = new DozerBeanMapper();

    public QuizService(QuizRepository quizRepository, LeaderboardService leaderboardService, QuestionService questionService, ScoreService scoreService, FirebaseFileService firebaseFileService) {
        this.quizRepository = quizRepository;
        this.leaderboardService = leaderboardService;
        this.questionService = questionService;
        this.scoreService = scoreService;
        this.firebaseFileService = firebaseFileService;
    }

    public List<QuizEntity> getAllQuizes(){
        return quizRepository.findAll();
    }

    public QuizEntity saveQuiz(QuizDetailDTO quiz) throws Exception {
        if (LocalDateTime.now().isAfter(LocalDateTime.ofInstant(quiz.getEnding().toInstant(), ZoneId.systemDefault()))) throw new BadRequestException("Ending can't be after today");
        if (quiz.getTheme() == null || quiz.getTitle() == null || quiz.getType() == null || quiz.getTheme().equals("") || quiz.getTitle().equals("")) throw new BadRequestException("All fields must be filled");

        QuizEntity newQuiz = mapper.map(quiz, QuizEntity.class);

        LeaderboardEntity newLeaderboard = mapper.map(new Leaderboard(), LeaderboardEntity.class);
        leaderboardService.saveLeaderboard(newLeaderboard);
        newQuiz.setLeaderBoard(newLeaderboard);
        return this.quizRepository.save(setDifficulty(newQuiz));

    }

    public void updateQuiz(String id, QuizDetailDTO quizDetailDto){
        if (LocalDateTime.now().isAfter(LocalDateTime.ofInstant(quizDetailDto.getEnding().toInstant(), ZoneId.systemDefault()))) throw new BadRequestException("Ending can't be after today");
        if (quizDetailDto.getType() == null || quizDetailDto.getTheme() == null || quizDetailDto.getTitle() == null  || quizDetailDto.getTheme().equals("") || quizDetailDto.getTitle().equals("")) throw new BadRequestException("Not all fields provided for Quiz on update");

        QuizEntity toBeUpdated = this.quizRepository.getById(id);
        toBeUpdated.setBeginning(quizDetailDto.getBeginning());
        toBeUpdated.setEnding(quizDetailDto.getEnding());
        toBeUpdated.setTheme(quizDetailDto.getTheme());
        toBeUpdated.setType(quizDetailDto.getType());
        toBeUpdated.setTitle(quizDetailDto.getTitle());

        this.quizRepository.save(toBeUpdated);
    }

    public int getAmountOfQuestions(String id){
        return this.quizRepository.getById(id).getQuestions().size();
    }

    public QuizEntity getSpecificQuiz(String id) throws Exception {
        QuizEntity quiz = this.quizRepository.findById(id).get();
        if(quiz == null){
            throw new Exception("Quiz not found");
        }
        return quiz;
    }

    public void deleteQuiz(String id) throws Exception {
        QuizEntity quiz = this.quizRepository.findById(id).get();
        if(quiz == null){
            throw new Exception("No such quiz in DB");
        }
        this.questionService.deleteQuestionsForQuiz(quiz.getId());
        this.quizRepository.delete(quiz);
        this.scoreService.deleteScoresFromLeaderboard(quiz.getLeaderBoard());
        this.leaderboardService.deleteLeaderboard(quiz.getLeaderBoard());
    }

    public void updateDifficulty(QuizEntity entity) throws Exception {
        entity = setDifficulty(entity);
        this.quizRepository.save(entity);
    }
    public void addImage(String id, MultipartFile file) throws IOException {
        if (file == null) throw new BadRequestException("Image was not provided");
        if (id == null || id.equals("")) throw new BadRequestException("Quiz id was not provided");

        QuizEntity entity = this.quizRepository.getById(id);
        entity.setQuizImage(this.firebaseFileService.saveImage(file, id));
        this.quizRepository.save(entity);
    }

    public QuizEntity setDifficulty(QuizEntity entity) throws Exception {
        List<QuestionEntity> questions = questionService.getQuestionsForQuiz(entity);
        long easyOccurences = questions.stream().filter(q -> q.getDifficulty().equals(Difficulty.easy)).count();
        double mediumOccurences = (questions.stream().filter(q -> q.getDifficulty().equals(Difficulty.medium)).count()) * 2;
        long hardOccurences = (questions.stream().filter(q -> q.getDifficulty().equals(Difficulty.hard)).count()) * 3;

        double calculatedDifficulty = (easyOccurences + mediumOccurences + hardOccurences) / questions.size();

        if(calculatedDifficulty < 1.75){
            entity.setDifficulty(Difficulty.easy);
        }else if(calculatedDifficulty < 2.25){
            entity.setDifficulty(Difficulty.medium);
        }else{
            entity.setDifficulty(Difficulty.hard);
        }
        return entity;
    }
}
