# docker-registry-web

Web UI, authentication service and event recorder for private docker registry v2.

## Features:

  * Browsing repositories, tags and images in docker registry v2
  * Optional token based authentication provider with role based permissions
  * Docker registry notification recording and audit

### Docker pull command
    
    docker pull hyper/docker-registry-web
        
### How to run

#### Without authentication
 
 1. Create configuration file `config.yml`
        
        registry:
          # Docker registry url
          url: http://registry:5000/v2
          # Docker registry fqdn
          name: localhost:5000
          # To allow image delete, should be false
          readonly: false
          auth:
            # Disable authentication
            enabled: false
      
  2. Run with docker

          docker run -i -t -p 8080:8080 --link registry -v ./config.yml:/config/config.yml hyper/docker-registry-web

  3. Web UI will be available on `http://localhost:8080`
  
#### With authentication enabled

 Token authentication requires RSA private key in PEM format and certificate matched with this key
 
 1. Generate private key and certificate
        
        mkdir conf
        openssl req -new -newkey rsa:4096 -days 365 -subj "/CN=localhost" \
                -nodes -x509 -keyout conf/auth.key -out conf/auth.cert
 
 2. Mount `conf/auth.cert` file to docker registry container. 
 
 3. Add following fragment to your registry configuration file:
            
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
                         
 4. Create configuration file `conf/config.yml`
        
        registry:
          # Docker registry url
          url: http://registry:5000/v2
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
            key: /config/auth.key
     
 5. Run with docker
 		docker run -i -t -p 8080:8080 --link registry -v ./conf:/config/ -v ./db:/data hyper/docker-registry-web
 
 6. Web UI will be available on `http://localhost:8080` with default admin user/password `admin/admin`.
 
### Role system 
 
After first start you will have following roles:

- UI_ADMIN
- UI_USER
- read-all
- write-all

You can't delete or modify UI_ADMIN and UI_USER role, they are special roles and allows admin or user access to UI respectivetly.  
User access allows to browse and delete registry, admin access allows to create, delete and modify users and roles in addition to user access.
Every non-special role have a list of ACLs, each of ACL grants permission grants permission to `pull`, `pull+push` or `pull+push+delete` 
based on IP and image name glob matching.
For example **read-all** role matches any IP and any image name with glob `*` and grants `pull` permission and
**write-all** role grants `pull+push` permission for any IP and any image name. 

### [Configuration reference](web-app/WEB-INF/config.yml)

### [Docker Compose configuration examples](examples)