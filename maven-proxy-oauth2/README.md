# maven-proxy-oauth2

This module adds authentication service for those backends that require oauth2 authentication. That requires for the
provider specific modules to implement the service interface `AuthorizationFlowManager`.

## Usage

To clean the cached tokens use:

    curl -X DELETE localhost:9000/auth
    
To get an authentification url to retrieve the authentication token from there:

    curl -X GET localhost:9000/auth
    
To commit an authentication token

    curl -X POST -H "Content-Type: text/plain" -d "<token here>" localhost:9000/auth