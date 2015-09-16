# docker-registry-web

Web UI for private docker registry v2.

### Docker pull command
    
    docker pull hyper/docker-registry-web
        
### [Demo](http://mkuchin.github.io/)
        
       
### How to run 
##### With docker

    docker run -i -t -p 8080:8080 -e REGISTRY_HOST=172.17.0.1 -e REGISTRY_PORT=5000 hyper/docker-registry-web

or
    
    docker run -i -t -p 8080:8080 --link registry -e REGISTRY_HOST=registry -e REGISTRY_PORT=5000 hyper/docker-registry-web
and open http://localhost:8080
##### With docker-compose
Download example [docker-compose.yml](https://raw.githubusercontent.com/mkuchin/docker-registry-web/master/docker-compose.yml), change path to registry volume and start bundle with 

    docker-compose up
    
### Environment variables
* REGISTRY_HOST - hostname of docker registry, if registry running in docker should be link name or point to container internal ip address
* REGISTRY_PORT - port of docker registry
* READONLY = true|false - readonly mode