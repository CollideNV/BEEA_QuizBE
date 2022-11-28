package be.collide.quizbackend.domain;

import be.collide.quizbackend.converter.LocalDateTimeToStringTypeConverter;
import be.collide.quizbackend.converter.UUIDToStringConverter;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Jacksonized
@Getter
@Setter
@Builder(toBuilder = true)
@DynamoDbBean
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class QuizAnswer {
    @Builder.Default
    private UUID quizId = UUID.randomUUID();
    @Builder.Default
    private UUID questionId = UUID.randomUUID();
    @Builder.Default
    private UUID answerId = UUID.randomUUID();
    private String user;
    private String primaryKey;
    @Builder.Default
    private LocalDateTime answerTime = LocalDateTime.now();


    @DynamoDbAttribute("primaryKey")
    @DynamoDbPartitionKey
    public String getPrimaryKey() {
        if (primaryKey == null)
            primaryKey = quizId.toString() + "#" + questionId.toString();
        return primaryKey;
    }

    @DynamoDbConvertedBy(UUIDToStringConverter.class)
    @DynamoDbAttribute("quizId")
    public UUID getQuizId() {
        return quizId;
    }


    @DynamoDbConvertedBy(UUIDToStringConverter.class)

    @DynamoDbAttribute("questionId")
    public UUID getQuestionId() {
        return questionId;
    }

    @DynamoDbConvertedBy(UUIDToStringConverter.class)
    @DynamoDbAttribute("answerId")
    public UUID getAnswerId() {
        return answerId;
    }

    @DynamoDbConvertedBy(LocalDateTimeToStringTypeConverter.class)
    @DynamoDbAttribute("answerTime")
    public LocalDateTime getAnswerTime() {
        return answerTime;
    }

    @DynamoDbAttribute("user")
    @DynamoDbSortKey
    public String getUser() {
        return user;
    }
}
