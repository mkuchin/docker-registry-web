# docker-registry-web

Web UI for private docker registry v2.

### Docker pull command
    
    docker pull hyper/docker-registry-web
        
### [Demo](http://mkuchin.github.io/)
        
       
### How to run 
##### With docker

    docker run -i -t -p 8080:8080 -e REGISTRY_HOST=172.17.0.1 -e REGISTRY_PORT=5000 -e REGISTRY_AUTH="YWRtaW46Y2hhbmdlbWU=" hyper/docker-registry-web

or
    
    docker run -i -t -p 8080:8080 --link registry -e REGISTRY_HOST=registry -e REGISTRY_PORT=5000 -e REGISTRY_AUTH="YWRtaW46Y2hhbmdlbWU=" hyper/docker-registry-web
and open http://localhost:8080
##### With docker-compose
Download example [docker-compose.yml](https://raw.githubusercontent.com/mkuchin/docker-registry-web/master/docker-compose.yml), change path to registry volume and start bundle with 

    docker-compose up
    
### Environment variables
* REGISTRY_HOST - hostname of docker registry, if registry running in docker should be link name or point to container internal ip address
* REGISTRY_PORT - port of docker registry
* REGISTRY_NAME - visible name of registry if it different from `${REGISTRY_HOST}:${REGISTRY_PORT}`
* REGISTRY_AUTH - base64 encoded token for basic authentication 
* READONLY = true|false - readonly mode
* TRUST_ANY_SSL = true|false - set to true, if you are using self signed certificate for registry
* CONTEXT_PATH - url prefix if you need to host web registry on non-root path.