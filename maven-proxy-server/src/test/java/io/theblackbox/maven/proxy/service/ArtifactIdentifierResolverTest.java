package io.theblackbox.maven.proxy.service;

import io.theblackbox.maven.proxy.model.ArtifactIdentifier;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by guillermoblascojimenez on 17/04/15.
 */
public class ArtifactIdentifierResolverTest {

    private static ArtifactIdentifierResolver artifactIdentifierResolver;

    @Before
    public void setup() {
        artifactIdentifierResolver = new ArtifactIdentifierResolver();
    }

    @Test
    public void test1() {
        String path = "com/google/appengine/appengine-api-1.0-sdk/1.9.19/appengine-api-1.0-sdk-1.9.19.jar";
        ArtifactIdentifier artifactIdentifier = artifactIdentifierResolver.resolve(path);
        Assert.assertNotNull(artifactIdentifier);
        Assert.assertEquals("appengine-api-1.0-sdk", artifactIdentifier.getArtifactId());
        Assert.assertEquals("com.google.appengine", artifactIdentifier.getGroupId());
        Assert.assertEquals("1.9.19", artifactIdentifier.getVersion());
        Assert.assertEquals("jar", artifactIdentifier.getPackaging());
    }

    @Test
    public void test2() {
        String path = "org/apache/jackrabbit/oak-commons/1.2.0/oak-commons-1.2.0.jar";
        ArtifactIdentifier artifactIdentifier = artifactIdentifierResolver.resolve(path);
        Assert.assertNotNull(artifactIdentifier);
        Assert.assertEquals("oak-commons", artifactIdentifier.getArtifactId());
        Assert.assertEquals("org.apache.jackrabbit", artifactIdentifier.getGroupId());
        Assert.assertEquals("1.2.0", artifactIdentifier.getVersion());
        Assert.assertEquals("jar", artifactIdentifier.getPackaging());
    }

    @Test
    public void test3() {
        String path = "net/liftweb/lift-webkit_2.9.1-1/2.6-RC2/lift-webkit_2.9.1-1-2.6-RC2.jar";
        ArtifactIdentifier artifactIdentifier = artifactIdentifierResolver.resolve(path);
        Assert.assertNotNull(artifactIdentifier);
        Assert.assertEquals("lift-webkit_2.9.1-1", artifactIdentifier.getArtifactId());
        Assert.assertEquals("net.liftweb", artifactIdentifier.getGroupId());
        Assert.assertEquals("2.6-RC2", artifactIdentifier.getVersion());
        Assert.assertEquals("jar", artifactIdentifier.getPackaging());
    }

}