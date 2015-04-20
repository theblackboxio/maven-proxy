package io.theblackbox.maven.proxy.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by guillermoblascojimenez on 18/04/15.
 */
@Configuration
public class GoogleDriveBackendServicesFactory {

    @Bean
    public HttpTransport getHttpTransport() {
        return new NetHttpTransport();
    }

    @Bean
    public JsonFactory getJsonFactory() {
        return JacksonFactory.getDefaultInstance();
    }

    @Bean
    public TokenPersistence getTokenPersistence(@Value("${googleDrive.cacheFileName}") String cacheFileName) {
        return new TokenPersistence.FileTokenPersistence(cacheFileName);
    }

    @Bean
    public Credential getCredential(TokenPersistence tokenPersistence, JsonFactory jsonFactory, HttpTransport httpTransport, GoogleClientSecrets googleClientSecrets) throws IOException {
        tokenPersistence.load();
        String refreshToken = tokenPersistence.getToken();
        Credential credential = new GoogleCredential.Builder()
                .setJsonFactory(jsonFactory)
                .setTransport(httpTransport)
                .setClientSecrets(googleClientSecrets)
                .build();
        credential.setRefreshToken(refreshToken);
        return credential;
    }

    @Bean
    public Drive getDrive(HttpTransport httpTransport, JsonFactory jsonFactory, Credential credential) {
        return new Drive.Builder(httpTransport, jsonFactory, credential).build();
    }

    @Bean
    public GoogleClientSecrets getGoogleClientSecrets(@Value("${googleDrive.clientSecretFileName}") String clientSecretFileName, JsonFactory jsonFactory) {
        File file = new File(clientSecretFileName);
        FileReader fileReader = null;
        GoogleClientSecrets googleClientSecrets = null;
        try {
            fileReader = new FileReader(file);
            googleClientSecrets = GoogleClientSecrets.load(jsonFactory, fileReader);
        } catch (IOException e) {
            throw new Error(e);
        } finally {
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return googleClientSecrets;
    }

    @Bean
    public ArtifactResolver buildBackendArtifactResolver(@Value("${googleDrive.repositoryId}") String repositoryId, Drive drive) {
        return new GoogleDriveBackendArtifactResolver(repositoryId, drive, true);
    }

    @Bean
    public AuthorizationFlowManager buildBackendAuthorizationFlowManager(TokenPersistence tokenPersistence, JsonFactory jsonFactory, HttpTransport httpTransport, GoogleClientSecrets googleClientSecrets, @Value("${googleDrive.redirectUri}") String redirectUri) {
        return new GoogleCredentialManager(tokenPersistence, jsonFactory, httpTransport, googleClientSecrets, redirectUri);
    }
    @Bean
    public BackendIdentityService buildBackendIdentityService(Drive drive) {
        return  new GoogleDriveBackendIdentityService(drive);
    }
}


