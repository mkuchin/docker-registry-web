# This repo is a fork version with some enhancement of this awesome repo https://github.com/mkuchin/docker-registry-web 

# docker-registry-web

Web UI, authentication service and event recorder for private docker registry v2.

[![Docker Stars](https://img.shields.io/docker/stars/lukenvn/docker-registry-web.svg?maxAge=86400)](https://hub.docker.com/r/lukenvn/docker-registry-web/) [![Docker Pulls](https://img.shields.io/docker/pulls/lukenvn/docker-registry-web.svg?maxAge=86400)](https://hub.docker.com/r/lukenvn/docker-registry-web/)

## Features:

  * Browsing repositories, tags and images in docker registry v2
  * Optional token based authentication provider with role-based permissions
  * Docker registry notification recording and audit

### Warning: [this version config](https://github.com/lukenvn/docker-registry-web/blob/master/web-app/WEB-INF/config.yml) is not compatible with configuration of versions prior 0.1.0
   [Migrating configuration from 0.0.4 to 0.1.x](https://github.com/lukenvn/docker-registry-web/wiki/Migrating-configuration) 

### Docker pull command
    
    docker pull lukenvn/docker-registry-web
        
### How to run

#### Quick start (config with environment variables, no authentication)

Do not use _registry_ as registry container name, it will break `REGISTRY_NAME` environment variable.
     
    docker run -d -p 5000:5000 --name registry-srv registry:2
    docker run -it -p 8080:8080 --name registry-web --link registry-srv -e REGISTRY_URL=http://registry-srv:5000/v2 -e REGISTRY_NAME=localhost:5000 lukenvn/docker-registry-web 

#### Connecting to docker registry with basic authentication and self-signed certificate
    docker run -it -p 8080:8080 --name registry-web --link registry-srv \
               -e REGISTRY_URL=https://registry-srv:5000/v2 \
               -e REGISTRY_TRUST_ANY_SSL=true \
               -e REGISTRY_BASIC_AUTH="YWRtaW46Y2hhbmdlbWU=" \
               -e REGISTRY_NAME=localhost:5000 lukenvn/docker-registry-web
    

#### No authentication, with config file
 
 1. Create configuration file `config.yml`
    
    (Any property in this config may be overridden with environment variable, for example
     property `registry.auth.enabled` will become `REGISTRY_AUTH_ENABLED`)   
      
        registry:
          # Docker registry url
          url: http://registry-srv:5000/v2
          # Docker registry fqdn
          name: localhost:5000
          # To allow image delete, should be false
          readonly: false
          auth:
            # Disable authentication
            enabled: false
      
 2. Run with docker
        
        docker run -p 5000:5000 --name registry-srv -d registry:2
        docker run -it -p 8080:8080 --name registry-web --link registry-srv -v $(pwd)/config.yml:/conf/config.yml:ro lukenvn/docker-registry-web

 3. Web UI will be available on `http://localhost:8080` 
  
#### With authentication enabled

 Token authentication requires RSA private key in PEM format and certificate matched with this key
 
 1. Generate private key and certificate
        
        mkdir conf
        openssl req -new -newkey rsa:4096 -days 365 -subj "/CN=localhost" \
                -nodes -x509 -keyout conf/auth.key -out conf/auth.cert
 
 2. Create registry config `conf/registry-srv.yml`
        
        version: 0.1    
        
        storage:
          filesystem:
            rootdirectory: /var/lib/registry
            
        http:
          addr: 0.0.0.0:5000   
            
        auth:
          token:
            # external url to docker-web authentication endpoint
            realm: http://localhost:8080/api/auth
            # should be same as registry.name of registry-web
            service: localhost:5000
            # should be same as registry.auth.issuer of registry-web
            issuer: 'my issuer'
            # path to auth certificate
            rootcertbundle: /etc/docker/registry/auth.cert
            
 3. Start docker registry
         
        docker run -v $(pwd)/conf/registry-srv.yml:/etc/docker/registry/config.yml:ro \
                    -v $(pwd)/conf/auth.cert:/etc/docker/registry/auth.cert:ro -p 5000:5000  --name registry-srv -d registry:2    
                         
 4. Create configuration file `conf/registry-web.yml`
        
        registry:
          # Docker registry url
          url: http://registry-srv:5000/v2
          # Docker registry fqdn
          name: localhost:5000
          # To allow image delete, should be false
          readonly: false
          auth:
            # Enable authentication
            enabled: true
            # Token issuer
            # should equals to auth.token.issuer of docker registry
            issuer: 'my issuer'
            # Private key for token signing
            # certificate used on auth.token.rootcertbundle should signed by this key
            key: /conf/auth.key
     
 5. Start registry-web
 
        docker run -v $(pwd)/conf/registry-web.yml:/conf/config.yml:ro \
                   -v $(pwd)/conf/auth.key:/conf/auth.key -v $(pwd)/db:/data \
                   -it -p 8080:8080 --link registry-srv --name registry-web lukenvn/docker-registry-web
 
 6. Web UI will be available on `http://localhost:8080` with default admin user/password `admin/admin`.
 
### Role system 
 
After first start you will have following roles:

- UI_ADMIN
- UI_USER
- UI_DELETE
- read-all
- write-all

You can't delete or modify UI_ADMIN and UI_USER role, they are special roles and allows admin or user access to UI respectively.  
User access allows to browse registry, admin access allows to create, delete and modify users and roles in addition to user access.

UI_DELETE role allows deleting images in the UI based on ACLs. 

Every non-special role has a list of ACLs, each of ACL grants permission grants permission to `pull`, `pull+push` or `pull+push+delete` 
based on IP and image name [glob matching](https://github.com/lukenvn/docker-registry-web/wiki/Glob-matching).
For example **read-all** role matches any IP and any image name with glob `*` and grants `pull` permission and
**write-all** role grants `pull+push` permission for any IP and any image name. 

### [Configuration reference](https://github.com/lukenvn/docker-registry-web/blob/master/web-app/WEB-INF/config.yml)

### [Docker Compose configuration examples](https://github.com/lukenvn/docker-registry-web/tree/master/examples)
