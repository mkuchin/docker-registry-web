# docker-registry-web

Web UI for private docker registry v2.

###Docker pull command
    
    docker pull hyper/docker-registry-web
        
###How to run:
    docker run -i -t -p 8080:8080 -e REGISTRY_HOST=172.17.0.1 -e REGISTRY_PORT=5000 hyper/docker-registry-web
or
    
    docker run -i -t -p 8080:8080 -link registry -e REGISTRY_HOST=registry -e REGISTRY_PORT=5000 hyper/docker-registry-web
     
and open http://localhost:8080