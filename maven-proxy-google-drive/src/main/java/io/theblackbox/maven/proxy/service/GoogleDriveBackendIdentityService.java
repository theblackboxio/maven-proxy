package io.theblackbox.maven.proxy.service;

import com.google.api.services.drive.Drive;

import java.io.IOException;

/**
 * Created by guillermoblascojimenez on 19/04/15.
 */
public class GoogleDriveBackendIdentityService implements BackendIdentityService {

    private Drive drive;

    public GoogleDriveBackendIdentityService(Drive drive) {
        this.drive = drive;
    }

    @Override
    public String backendUser() throws IOException {
        return drive.about().get().execute().getName();
    }

}
