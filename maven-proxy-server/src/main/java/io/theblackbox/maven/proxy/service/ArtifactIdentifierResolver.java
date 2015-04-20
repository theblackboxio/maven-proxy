package io.theblackbox.maven.proxy.service;

import io.theblackbox.maven.proxy.model.ArtifactIdentifier;
import org.springframework.stereotype.Component;

/**
 * Created by guillermoblascojimenez on 17/04/15.
 */
@Component
public class ArtifactIdentifierResolver {

    private static final String PATH_SEPARATOR = "/";
    private static final String GROUP_ID_SEPARATOR = ".";
    private static final String EXTENSION_SEPARATOR = ".";

    public ArtifactIdentifier resolve(String path) {
        assert path != null;
        String[] split = path.split(PATH_SEPARATOR);
        int n = split.length;
        if (n < 4) {
            throw new IllegalArgumentException("Path too short \"" + path + "\".");
        }
        String fileName = split[n-1];
        String version = split[n-2];
        String artifactId = split[n-3];
        int tailLength = fileName.length() + version.length() + artifactId.length() + 3;
        String groupId = path.substring(0, path.length() - tailLength).replace(PATH_SEPARATOR, GROUP_ID_SEPARATOR);
        String packaging = fileName.substring(fileName.lastIndexOf(EXTENSION_SEPARATOR) +1, fileName.length());

        return new ArtifactIdentifier(artifactId, groupId, version, packaging);
    }


}
