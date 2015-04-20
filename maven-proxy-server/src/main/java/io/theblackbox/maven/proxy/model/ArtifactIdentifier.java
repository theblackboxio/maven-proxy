package io.theblackbox.maven.proxy.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Model class that represents an artifact identifier. A maven artifact is identified by these parameters:
 *  - artifactId, usually the key name of the artifact
 *  - groupId, usually the key name of the set of artifacts that the artifact belongs to
 *  - version
 *  - packaging
 *
 * The instances of this class are immutable.
 */
public class ArtifactIdentifier implements Serializable {

    private static final String GROUP_ID_SEPARATOR = ".";
    private static final String GROUP_ID_REGEX_SEPARATOR = "\\.";

    private static final String DIRECTORY_SEPARATOR = File.separator;

    private final String artifactId;

    private final String groupId;

    private final String version;

    private final String packaging;

    /**
     * Main constructor.
     * @param artifactId artifactId value of the artifact.
     * @param groupId groupId value of the artifact.
     * @param version version value of the artifact.
     * @param packaging packaging value of the artifact.
     */
    public ArtifactIdentifier(String artifactId, String groupId, String version, String packaging) {
        this.artifactId = artifactId;
        this.groupId = groupId;
        this.version = version;
        this.packaging = packaging;
    }

    public String getPackaging() {
        return packaging;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getVersion() {
        return version;
    }

    /**
     * Returns the file name of the artifact. The maven syntax is:
     *  {artifactId}-{version}.{packaging}
     * @return The maven syntax file name of this artifact.
     */
    public String getFileName() {
        return artifactId + "-" + version + "." + packaging;
    }

    /**
     * Returns the directory list of the groupId of this artifact. That is:
     *  - "x.y.z" => Arrays.asList("x","y","z")
     * @return the directory list of the groupId of this artifact.
     */
    public List<String> getDirectoryList() {
        String[] groupDirectories = this.getGroupId().split(GROUP_ID_REGEX_SEPARATOR);
        List<String> directories = new ArrayList<String>(Arrays.asList(groupDirectories));
        directories.add(this.getArtifactId());
        directories.add(this.getVersion());
        return directories;
    }

    public String getDirectory() {
        String dirs = this.getGroupId().replace(GROUP_ID_SEPARATOR, DIRECTORY_SEPARATOR);
        return dirs + DIRECTORY_SEPARATOR + this.getArtifactId() + DIRECTORY_SEPARATOR + this.getVersion() + DIRECTORY_SEPARATOR;
    }

    /**
     * Equals based on the equality of all main attributes:
     *  - artifactId
     *  - groupId
     *  - version
     *  - packaging
     *
     *  Equals is consistent with hashCode.
     *
     * @param o Object to compare with.
     * @return true if both equals in all main attributes, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArtifactIdentifier that = (ArtifactIdentifier) o;

        if (!artifactId.equals(that.artifactId)) return false;
        if (!groupId.equals(that.groupId)) return false;
        if (!version.equals(that.version)) return false;
        return packaging.equals(that.packaging);

    }

    /**
     * Hash code based on the hashcode of all main attributes:
     *  - artifactId
     *  - groupId
     *  - version
     *  - packaging
     *
     * Hashcode is consistent with equals.
     *
     * @return The hascode of the artifact.
     */
    @Override
    public int hashCode() {
        int result = artifactId.hashCode();
        result = 31 * result + groupId.hashCode();
        result = 31 * result + version.hashCode();
        result = 31 * result + packaging.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return groupId + ":" + artifactId + ":" + packaging + ":" + version;
    }
}
