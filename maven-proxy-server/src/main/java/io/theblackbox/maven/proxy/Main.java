package io.theblackbox.maven.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * Entry point of spring boot application.
 *
 * Packages to scan:
 *  - io.theblackbox.maven.proxy
 *
 */
@SpringBootApplication
@ComponentScan("io.theblackbox.maven.proxy")
public class Main {

    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = SpringApplication.run(Main.class, args);
    }

}