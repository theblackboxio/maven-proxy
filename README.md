# Maven Proxy

This project develops a proxy for maven repositories. The aim is to provide a bridge to other file storage providers
like Google Drive or Dropbox. Usually to have a private or public maven repository one could use Sonatype Nexus
that is for sure a great solution. The issue is that Nexus requires a server and configure all the stuff.

The application is split in the following kind of modules.

## Functional modules

Modules that implement a functionality but gives away the backend provider specific implementation details. Consider
that these modules are executable, but the startup shall fail due to Spring would not find components to inject.

* `maven-proxy-parent`: contains the dependency management and the application startup configuration in a single pom.

* `maven-proxy-server`: contains the programmatic logic of the proxy but the details of the artifact storage is not
provided.

* `maven-proxy-oauth2`: enables an api so the user can authorize to the proxy to connect to a backend provider
that requires oauth2. The implementation details of the oauth2 is not provided.

## Executable modules

These modules extends some of the functional modules (at least `maven-proxy-server`) and implements the backend
provider specific details. These modules are executable.

* `maven-proxy-file`: For demo and testing purposes, given a directory of the local file system drops the artifacts and takes them from
there. 

* `maven-proxy-google-drive`: Uses as file storage Google Drive. Requires an oauth2 authentification process.

# Run a proxy

Here you have an spinnet to run `maven-proxy-file`:

    cd maven-proxy-parent
    mvn package -am -pl ../maven-proxy-file
    java -jar ../maven-proxy-google-drive/target/maven-proxy-google-drive-1.0-SNAPSHOT.jar \
        --file.repositoryPath="${HOME}/mavenRepoTest/"
        
And the template would be:


    cd maven-proxy-parent
    mvn package -am -pl ../<executable module>
    java -jar ../<executable module>/target/<executable module>-<executable module version>.jar \
        <module specific configuration>
        <proxy general configuration>
        
You can find the module specific configuration in the readme of each module.

## General proxy configuration

See `maven-proxy-server/src/main/resources/application.properties` file to see the full list of options.

* server.port: 9000
* management.port: 9001
* management.address: 127.0.0.1

Overwrite all options from command line with two dashes, equals and quotes. Example:

    --server.port="9000"