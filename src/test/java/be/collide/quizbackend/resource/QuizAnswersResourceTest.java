package be.collide.quizbackend.resource;

import be.collide.quizbackend.domain.QuizAnswer;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class QuizAnswersResourceTest {

    @Inject
    DynamoDbEnhancedClient client;

    @BeforeEach
    void setUp() {
        DynamoDbTable<QuizAnswer> quizTable = client.table("Quizanswer", TableSchema.fromBean(QuizAnswer.class));

        quizTable.createTable(builder -> builder
                .provisionedThroughput(b -> b
                        .readCapacityUnits(10L)
                        .writeCapacityUnits(10L)
                        .build())
        );
    }

    @AfterEach
    void tearDown() {
        DynamoDbTable<QuizAnswer> quizTable = client.table("Quizanswer", TableSchema.fromBean(QuizAnswer.class));

        quizTable.deleteTable();
    }

    @Test
    void addAndGet() {

        UUID quizId = UUID.randomUUID();
        UUID questionId = UUID.randomUUID();
        UUID answerId = UUID.randomUUID();
        given()
                .contentType("application/json")
                .body(QuizAnswersResource.QuizAnswerDTO.builder()
                        .quizId(quizId)
                        .questionId(questionId)
                        .answerId(answerId)
                        .user("user")
                        .build())
                .when().post("/quiz-answers")
                .then().log().all()
                .statusCode(200);
        given()
                .contentType("application/json")
                .body(QuizAnswersResource.QuizAnswerDTO.builder()
                        .quizId(quizId)
                        .questionId(questionId)
                        .answerId(answerId)
                        .user("user2")
                        .build())
                .when().post("/quiz-answers")
                .then().log().all()
                .statusCode(200);
        given()
                .contentType("application/json")
                .body(QuizAnswersResource.QuizAnswerDTO.builder()
                        .quizId(quizId)
                        .questionId(questionId)
                        .answerId(answerId)
                        .user("user3")
                        .build())
                .when().post("/quiz-answers")
                .then().log().all()
                .statusCode(200);

        List<QuizAnswersResource.QuizAnswerDTO> retrievedAnswers = given()
                .when().get("/quiz-answers/" + quizId + "/" + questionId)
                .then()
                .extract().jsonPath().getList(".", QuizAnswersResource.QuizAnswerDTO.class);


        assertThat(retrievedAnswers).hasSize(3);


    }
}