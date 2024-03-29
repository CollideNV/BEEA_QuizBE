package be.collide.quizbackend.resource;

import be.collide.quizbackend.domain.QuizAnswer;
import be.collide.quizbackend.domain.QuizAnswers;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Path("/quiz-answers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class QuizAnswersResource {
    @Inject
    QuizAnswers quizAnswers;

    @POST
    public Response add(QuizAnswerDTO quizAnswer) {
        quizAnswers.addQuizAnswer(QuizAnswer.builder()
                .answerId(quizAnswer.answerId)
                .quizId(quizAnswer.quizId)
                .user(quizAnswer.user)
                .answerTime(quizAnswer.answerTime)
                .questionId(quizAnswer.questionId)
                .build());
        return Response.ok().build();
    }

    @GET
    @Path("/{quizId}/{questionId}")
    public List<QuizAnswerDTO> getAnswers(@PathParam("quizId") UUID quizId, @PathParam("questionId") UUID questionId) {
        return quizAnswers.findForQuestion(quizId, questionId).stream().map(quizAnswer -> new QuizAnswerDTO(quizAnswer.getQuizId(), quizAnswer.getQuestionId(), quizAnswer.getAnswerId(), quizAnswer.getUser(), quizAnswer.getAnswerTime())).toList();
    }

    @Builder
    @Jacksonized
    @Getter
    public static class QuizAnswerDTO {
        private UUID quizId;
        private UUID questionId;
        private UUID answerId;
        private String user;
        private LocalDateTime answerTime;
    }
}
