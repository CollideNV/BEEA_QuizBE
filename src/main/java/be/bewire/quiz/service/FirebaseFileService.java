package be.bewire.quiz.service;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Service
public class FirebaseFileService {
    final private Bucket bucket;

    FirebaseFileService() throws IOException {
        InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("google/cloud-messaging-key.json");

        assert serviceAccount != null;
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("who-is-who-270620.appspot.com")
                .build();
        FirebaseApp.initializeApp(options);

        bucket = StorageClient.getInstance().bucket();
    }
    public String saveImage(MultipartFile file, String id) throws IOException {
        URL url = bucket.create("quiz/"+ id, file.getInputStream(), file.getContentType()).signUrl(356, TimeUnit.DAYS);
        return url.toString();
    }

}
