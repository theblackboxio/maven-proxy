package io.theblackbox.maven.proxy.controller;

import io.theblackbox.maven.proxy.model.ArtifactIdentifier;
import io.theblackbox.maven.proxy.service.ArtifactIdentifierResolver;
import io.theblackbox.maven.proxy.service.ArtifactResolver;
import io.theblackbox.maven.proxy.service.StreamManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by guillermoblascojimenez on 17/04/15.
 */
@Controller
@RequestMapping("/repository")
public class RepositoryController {

    private static final Log LOG = LogFactory.getLog(RepositoryController.class);

    @Autowired
    private ArtifactIdentifierResolver artifactIdentifierResolver;

    @Autowired
    private ArtifactResolver artifactResolver;

    @Autowired
    private StreamManager streamManager;


    @RequestMapping(value = "/release/**", method = RequestMethod.GET)
    public void getReleaseArtifact(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = request.getRequestURI();
        String artifactPath = removePrefix("/repository/release/", url);
        ArtifactIdentifier artifactIdentifier = artifactIdentifierResolver.resolve(artifactPath);
        LOG.info("Requested download of artifact " + artifactIdentifier.toString());
        InputStream artifactInputStream = artifactResolver.resolveDownload(artifactIdentifier);
        if (artifactInputStream == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        } else {
            OutputStream responseOutputStream = response.getOutputStream();
            streamManager.copy(artifactInputStream, responseOutputStream);
            responseOutputStream.flush();
            responseOutputStream.close();
            artifactInputStream.close();
        }
        response.flushBuffer();
    }

    @RequestMapping(value = "/release/**", method = RequestMethod.PUT)
    public void putReleaseArtifact(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = request.getRequestURI();
        String artifactPath = removePrefix("/repository/release/", url);
        ArtifactIdentifier artifactIdentifier = artifactIdentifierResolver.resolve(artifactPath);
        LOG.info("Requested upload of artifact " + artifactIdentifier.toString());
        artifactResolver.resolveUpload(artifactIdentifier, request.getInputStream());
        response.setStatus(HttpStatus.OK.value());
        response.flushBuffer();
    }

    private static String removePrefix(String prefix, String target) {
        assert prefix != null;
        assert target != null;
        if (! target.startsWith(prefix)) {
            throw new IllegalArgumentException("Target \"" + target + "\" does not starts with prefix \"" + prefix + "\"");
        }
        return target.substring(prefix.length(), target.length());
    }
}
