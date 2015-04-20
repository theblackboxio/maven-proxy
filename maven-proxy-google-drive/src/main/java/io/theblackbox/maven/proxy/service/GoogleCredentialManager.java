package io.theblackbox.maven.proxy.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.drive.DriveScopes;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by guillermoblascojimenez on 12/04/15.
 */
public class GoogleCredentialManager extends AbstractAuthrizatinFlowManager implements AuthorizationFlowManager {

    private final TokenPersistence refreshTokenPersistence;

    private final JsonFactory jsonFactory;

    private final HttpTransport httpTransport;

    private final GoogleClientSecrets googleClientSecrets;

    private final String redirectUrl;

    private GoogleAuthorizationCodeFlow flow;

    private String url;

    public GoogleCredentialManager(
            TokenPersistence refreshTokenPersistence,
            JsonFactory jsonFactory,
            HttpTransport httpTransport,
            GoogleClientSecrets googleClientSecrets,
            String redirectUrl) {
        this.refreshTokenPersistence = refreshTokenPersistence;
        this.jsonFactory = jsonFactory;
        this.httpTransport = httpTransport;
        this.googleClientSecrets = googleClientSecrets;
        this.redirectUrl = redirectUrl;
    }


    @Override
    public void loadToken() throws IOException {
        refreshTokenPersistence.load();
    }


    @Override
    public boolean hasToken() {
        return refreshTokenPersistence.hasToken();
    }

    @Override
    public void clearToken() {
        refreshTokenPersistence.clear();
    }

    @Override
    public void startFlow() {
        if (!(getState().equals(State.READY) || getState().equals(State.DONE))) {
            throw new IllegalStateException();
        }
        this.flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, googleClientSecrets, DriveScopes.all())
                .setAccessType("offline") // ensure we have refresh token always
                .setApprovalPrompt("force")
                .build();
        setState(State.STARTED);
    }

    @Override
    public String getUrl() {
        if (getState().equals(State.URL_GENERATED)) {
            return this.url;
        }
        if (!getState().equals(State.STARTED)) {
            throw new IllegalStateException();
        }
        this.url = flow.newAuthorizationUrl().setRedirectUri(redirectUrl).build();
        setState(State.URL_GENERATED);
        return this.url;
    }

    @Override
    public void commitCode(String code) throws IOException {
        if (!getState().equals(State.URL_GENERATED)) {
            throw new IllegalStateException();
        }
        GoogleTokenResponse response = flow
                .newTokenRequest(code)
                .setRedirectUri(redirectUrl)
                .execute();
        String refreshToken = response.getRefreshToken();
        refreshTokenPersistence.persistToken(refreshToken);
        setState(State.DONE);
    }

    public static GoogleClientSecrets getGoogleClientSecrets(JsonFactory jsonFactory, String fileName) {
        File file = new File(fileName);
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


}
