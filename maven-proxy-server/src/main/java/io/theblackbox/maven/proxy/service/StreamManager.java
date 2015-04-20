package io.theblackbox.maven.proxy.service;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by guillermoblascojimenez on 17/04/15.
 */
@Component
public class StreamManager {

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4; // 4KB
    
    public void copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
    }
    
}
