package io.theblackbox.maven.proxy.service;

import io.theblackbox.maven.proxy.model.ArtifactIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * Created by guillermoblascojimenez on 20/04/15.
 */
@Component
public class FileArtifactResolver implements ArtifactResolver {


    private static final String DIRECTORY_SEPARATOR = File.separator;

    private String repositoryPath;

    private File repositoryFile;

    @Autowired
    private StreamManager streamManager;

    @Autowired
    public void setRepositoryPath(@Value("${file.repositoryPath}")String repositoryPath) {
        if (repositoryPath.endsWith(DIRECTORY_SEPARATOR)) {
            this.repositoryPath = repositoryPath;
        } else {
            this.repositoryPath = repositoryPath + DIRECTORY_SEPARATOR;
        }
        this.repositoryFile = new File(repositoryPath);
        if (!this.repositoryFile.exists()) {
            this.repositoryFile.mkdirs();
        }
    }

    @Override
    public InputStream resolveDownload(ArtifactIdentifier artifactIdentifier) throws IOException {

        String fileName = repositoryPath + artifactIdentifier.getDirectory() + artifactIdentifier.getFileName();
        File file = new File(fileName);
        if (file.exists()) {
            return new FileInputStream(file);
        } else {
            return null;
        }

    }

    @Override
    public void resolveUpload(ArtifactIdentifier artifactIdentifier, InputStream inputStream) throws IOException {

        String dir = repositoryPath + artifactIdentifier.getDirectory();
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        String fileName = repositoryPath + artifactIdentifier.getDirectory() + artifactIdentifier.getFileName();
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }

        file.createNewFile();
        OutputStream outputStream = new FileOutputStream(file);
        streamManager.copy(inputStream, outputStream);
        outputStream.flush();
        outputStream.close();

    }
}
