# maven-proxy-google-drive

This module uses Google Drive as backend for artifact persistence. Also allows to authentificate access with oauth2. 
See modules `maven-proxy-server` and `maven-proxy-oauth2` for more api details.

## Configuration

* googleDrive.repositoryId="<your repo id>"

* googleDrive.clientSecretFileName="${HOME}/.m2/gdriverepo_client_secret.json"

* googleDrive.cacheFileName="${HOME}/.m2/gdriverepo-token-cache.txt"