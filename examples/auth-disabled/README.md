## Example configuration of docker registry and docker-registry-web without authorization
### How to run:

1. Download and extract example files
        
        curl -Ls https://github.com/mkuchin/docker-registry-web/releases/download/v0.1.0/examples.tar.gz | tar -xzv
        cd examples/auth-disabled/
    
2. Start containers with docker-compose    
    
        docker-compose up

It will run docker registry `localhost:5000` and web ui on `http://localhost:8080/`