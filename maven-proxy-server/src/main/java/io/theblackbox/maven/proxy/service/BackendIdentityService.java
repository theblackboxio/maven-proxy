package io.theblackbox.maven.proxy.service;

import java.io.IOException;

/**
 *
 */
public interface BackendIdentityService {

    String backendUser() throws IOException;

}
