package io.theblackbox.maven.proxy.service;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;
import io.theblackbox.maven.proxy.model.ArtifactIdentifier;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * Created by guillermoblascojimenez on 18/04/15.
 */
public class GoogleDriveBackendArtifactResolver implements ArtifactResolver {

    private final Drive drive;

    private final String repositoryId;

    private final boolean overwrite;

    public GoogleDriveBackendArtifactResolver( String repositoryId, Drive drive, boolean overwrite) {
        this.drive = drive;
        this.repositoryId = repositoryId;
        this.overwrite = overwrite;
    }

    public Drive getDrive() {
        return drive;
    }

    public String getRepositoryId() {
        return repositoryId;
    }


    @Override
    public InputStream resolveDownload(ArtifactIdentifier artifactIdentifier) throws IOException {

        List<String> directories = artifactIdentifier.getDirectoryList();

        // generate the directory structure
        String parentId = getRepositoryId();
        for (String childTitle : directories) {
            parentId = getChildren(parentId, childTitle, false);
            if (parentId == null) {
                return null;
            }
        }

        // upload the artifact
        return downloadArtifact(parentId, artifactIdentifier);
    }

    private InputStream downloadArtifact(String parentId, ArtifactIdentifier artifactIdentifier) throws IOException {
        String fileName = artifactIdentifier.getFileName();
        FileList list = getFileWithTitleAndParent(fileName, parentId).execute();
        if (list.size() == 0) {
            return null;
        }
        if (list.size() > 1) {
            throw new IllegalAccessError("Multiple files found for artifact " + artifactIdentifier.toString());
        }
        File file = list.getItems().get(0);
        if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0) {
            try {
                // uses alt=media query parameter to request content
                return drive.files().get(file.getId()).executeMediaAsInputStream();
            } catch (IOException e) {
                // An error occurred.
                e.printStackTrace();
                return null;
            }
        } else {
            // The file doesn't have any content stored on Drive.
            return null;
        }
    }

    @Override
    public void resolveUpload(ArtifactIdentifier artifactIdentifier, InputStream artifactFile) throws IOException {

        List<String> directories = artifactIdentifier.getDirectoryList();

        // generate the directory structure
        String parentId = getRepositoryId();
        for (String childTitle : directories) {
            parentId = getChildren(parentId, childTitle, true);
        }

        // upload the artifact
        uploadArtifact(parentId, artifactIdentifier, artifactFile);

    }

    private String uploadArtifact(String parentId, ArtifactIdentifier artifactIdentifier, InputStream artifactFile) throws IOException {
        // test if artifact exists
        Drive.Files.List request = getFileWithTitleAndParent(artifactIdentifier.getFileName(), parentId);
        FileList files = request.execute();
        boolean exists = files.size() > 0;

        if (!overwrite && exists) {
            throw new IllegalAccessError("Tried to upload an artifact " + artifactIdentifier + "that was already uploaded with overwrite deactivated.");
        }

        if (exists) {
            // remove files with same name
            do {
                try {

                    for (File child : files.getItems()) {
                        getDrive().files().delete(child.getId()).execute();
                    }
                    request.setPageToken(files.getNextPageToken());
                } catch (IOException e) {
                    System.out.println("An error occurred: " + e);
                    request.setPageToken(null);
                }
            } while (request.getPageToken() != null &&
                    request.getPageToken().length() > 0);
        }

        ParentReference parentReference = new ParentReference();
        parentReference.setId(parentId);
        File body = new File();
        body.setTitle(artifactIdentifier.getFileName());
        body.setMimeType("application/java-archive");
        body.setParents(Collections.singletonList(parentReference));

        InputStreamContent fileContent = new InputStreamContent("application/java-archive", artifactFile);

        File file = getDrive().files().insert(body, fileContent).execute();
        return file.getId();
    }


    /**
     * Given a parentId and a child name tests if exists a child for the parent with given parent id then
     * returns the id of the child, otherwise creates the child with the given title and returns the child id.
     * @param parentId
     * @param childName
     * @return
     * @throws IOException
     */
    private String getChildren(String parentId, String childName, boolean create) throws IOException {

        Drive.Files.List request = getFileWithTitleAndParent(childName, parentId);

        String childId = null;
        do {
            try {
                FileList children = request.execute();

                for (File child : children.getItems()) {
                    if (childName.equals(child.getTitle())) {
                        childId = child.getId();
                    }
                }
                request.setPageToken(children.getNextPageToken());
            } catch (IOException e) {
                System.out.println("An error occurred: " + e);
                request.setPageToken(null);
            }
        } while (request.getPageToken() != null &&
                request.getPageToken().length() > 0);

        if (create && childId == null) {
            ParentReference parentReference = new ParentReference();
            parentReference.setId(parentId);
            File body = new File();
            body.setTitle(childName);
            body.setParents(Collections.singletonList(parentReference));
            body.setMimeType("application/vnd.google-apps.folder");
            File file = getDrive().files().insert(body).execute();
            childId = file.getId();
        }
        return childId;
    }

    private Drive.Files.List getFileWithTitleAndParent(String fileTitle, String parentId) throws IOException {
        return getDrive()
                .files()
                .list()
                .setQ("title = \'" + fileTitle + "\' and \'" + parentId + "\' in parents and trashed = false");
    }

}
