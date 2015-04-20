# maven-proxy-server

This core module declares the published url of the repository and implements a basic logic delegating the backend
provider details.

## Delegation

The delegated interfaces are:

* `io.theblackbox.maven.proxy.service.ArtifactResolver` that requires two methods:

- resolveUpload

- resolveDownload

## Configuartion

