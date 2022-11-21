package be.collide.quizbackend.resource;

import be.collide.quizbackend.domain.QuizAnswer;
import be.collide.quizbackend.domain.QuizAnswers;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.UUID;

@Path("/quiz-answers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class QuizAnswersResource {
    @Inject
    QuizAnswers quizAnswers;

    @POST
    public Response add(QuizAnswer quizAnswer) {
        quizAnswers.addQuizAnswer(quizAnswer);
        return Response.ok().build();
    }

    @GET
    @Path("/{quizId}/{questionId}")
    public List<QuizAnswer> getAnswers(@PathParam("quizId") UUID quizId, @PathParam("questionId") UUID questionId) {
        return quizAnswers.findForQuestion(quizId, questionId);
    }
}
