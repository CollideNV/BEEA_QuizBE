package be.bewire.quiz.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/authenticate")
@Slf4j
public class AuthenticationController {

    private final Environment env;

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    public AuthenticationController(Environment env) {
        this.env = env;
    }

    @PostMapping("/google")
    public HttpStatus authenticateGoogleLogin(@RequestBody String idToken) throws IOException {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(env.getProperty("spring.security.oauth2.client.registration.google.client-id")))
                .build();
        GoogleIdToken googleIdToken = null;
        try {
            // Gebruik de verifier om de idtoken te verify'en (krijg je een GoogleIdToken van terug)
            googleIdToken = verifier.verify(idToken);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            logger.info("Unable to get JWT Token");
        } catch (ExpiredJwtException e) {
            logger.info("JWT Token has expired");
        } catch (GeneralSecurityException e) {
            logger.info("General security in danger");
        }
        // Check dat de token niet null is.
        if (googleIdToken == null) {
            // Return ok wanneer token niet null is en een error code (400 of 401) als de token wel null is
            return HttpStatus.UNAUTHORIZED;
        }

        return HttpStatus.OK;
    }
}
