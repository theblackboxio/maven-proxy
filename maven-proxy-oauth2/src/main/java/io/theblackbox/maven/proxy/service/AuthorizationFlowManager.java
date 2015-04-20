package io.theblackbox.maven.proxy.service;

import java.io.IOException;

/**
 * Created by guillermoblascojimenez on 18/04/15.
 */
public interface AuthorizationFlowManager {

    void loadToken() throws IOException;

    boolean hasToken();

    void clearToken() throws IOException;

    enum State {
        READY, STARTED, URL_GENERATED, DONE
    }

    State getState();

    void startFlow();

    String getUrl();

    void commitCode(String code) throws IOException;

}
