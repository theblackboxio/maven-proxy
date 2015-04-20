package io.theblackbox.maven.proxy.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by guillermoblascojimenez on 20/04/15.
 */
public class ArtifactIdentifierTest {


    private static final ArtifactIdentifier APP_ENGINE_API_ARTIFACT_ID = new ArtifactIdentifier(
            "appengine-api-1.0-sdk",
            "com.google.appengine",
            "1.9.19",
            "jar"
    );

    private static final ArtifactIdentifier OAK_COMMONS_ARTIFACT_ID = new ArtifactIdentifier(
            "oak-commons",
            "org.apache.jackrabbit",
            "1.2.0",
            "jar"
    );


    private static final ArtifactIdentifier LIFT_WEBKIT_ARTIFACT_ID = new ArtifactIdentifier(
            "lift-webkit_2.9.1-1",
            "net.liftweb",
            "2.6-RC2",
            "jar"
    );



    @Test
    public void testDirectoryListAppEngineApiArtifactId() {
        List<String> actual = APP_ENGINE_API_ARTIFACT_ID.getDirectoryList();
        List<String> expected = Arrays.asList("com", "google", "appengine", "appengine-api-1.0-sdk", "1.9.19");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDirectoryListOakCommonsArtifactId() {
        List<String> actual = OAK_COMMONS_ARTIFACT_ID.getDirectoryList();
        List<String> expected = Arrays.asList("org", "apache", "jackrabbit", "oak-commons", "1.2.0");
        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testDirectoryListLiftWebkitArtifactId() {
        List<String> actual = LIFT_WEBKIT_ARTIFACT_ID.getDirectoryList();
        List<String> expected = Arrays.asList("net", "liftweb", "lift-webkit_2.9.1-1", "2.6-RC2");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFileNameAppEngineApiArtifactId() {
        String actual = APP_ENGINE_API_ARTIFACT_ID.getFileName();
        Assert.assertEquals("appengine-api-1.0-sdk-1.9.19.jar", actual);
    }
    @Test
    public void testFileNameOakCommonsArtifactId() {
        String actual = OAK_COMMONS_ARTIFACT_ID.getFileName();
        Assert.assertEquals("oak-commons-1.2.0.jar", actual);
    }
    @Test
    public void testFileNameLiftWebkitArtifactId() {
        String actual = LIFT_WEBKIT_ARTIFACT_ID.getFileName();
        Assert.assertEquals("lift-webkit_2.9.1-1-2.6-RC2.jar", actual);
    }



}