package be.collide.quizbackend.service;

import be.collide.quizbackend.domain.QuizAnswer;
import be.collide.quizbackend.domain.QuizAnswers;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class QuizAnswersService implements QuizAnswers {

    private DynamoDbTable<QuizAnswer> quizAnswerTable;


    @Inject
    DynamoDbEnhancedClient client;

    @PostConstruct
    void postConstruct() {
        quizAnswerTable = client.table("Quizanswer", TableSchema.fromBean(QuizAnswer.class));
    }

    @Override
    public List<QuizAnswer> findForQuestion(UUID quizId, UUID questionId) {
        String partitionValue = quizId.toString() + "#" + questionId.toString();
        Key key = Key.builder().partitionValue(partitionValue).build();

        return quizAnswerTable.query(QueryEnhancedRequest.builder().queryConditional(QueryConditional.keyEqualTo(key)).build()).items().stream().toList();
    }

    @Override
    public void addQuizAnswer(QuizAnswer quizAnswer) {
        quizAnswerTable.putItem(quizAnswer);
    }
}
