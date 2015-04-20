package io.theblackbox.maven.proxy.service;

import java.io.*;

/**
 * Created by guillermoblascojimenez on 12/04/15.
 */
public interface TokenPersistence {

    void persistToken(String token) throws IOException;

    boolean hasToken();

    String getToken();

    void load() throws IOException;

    void clear();

    class MemoryTokenPersistence implements TokenPersistence {

        private volatile String token;

        public void persistToken(String token) {
            this.token = token;
        }

        public boolean hasToken() {
            return token != null;
        }

        public String getToken() {
            return token;
        }

        public void clear() {
            this.token = null;
        }

        public void load() {

        }
    }

    class FileTokenPersistence implements TokenPersistence {

        private final File file;
        private volatile String token;

        public FileTokenPersistence(String file) {
            this.file = new File(file);
        }
        public FileTokenPersistence(File file) {
            this.file = file;
        }

        public void persistToken(String token) throws IOException {
            boolean alreadyExists = ! file.createNewFile();
            FileWriter fileWriter = new FileWriter(file, false); // overwrite
            fileWriter.write(token.toCharArray());
            fileWriter.flush();
            fileWriter.close();
        }

        public boolean hasToken() {
            return token != null;
        }

        public String getToken() {
            return token;
        }

        public void clear() {
            boolean success = file.delete();
        }

        public void load() throws IOException {
            if (!file.exists()) {
                this.token = null;
            } else {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String token = bufferedReader.readLine();
                this.token = token;
                fileReader.close();
                bufferedReader.close();
            }
        }
    }

}
