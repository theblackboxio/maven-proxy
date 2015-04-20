package io.theblackbox.maven.proxy.service;

import io.theblackbox.maven.proxy.model.ArtifactIdentifier;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by guillermoblascojimenez on 17/04/15.
 */
public interface ArtifactResolver {

    InputStream resolveDownload(ArtifactIdentifier artifactIdentifier) throws IOException;

    void resolveUpload(ArtifactIdentifier artifactIdentifier, InputStream inputStream) throws IOException;

}
