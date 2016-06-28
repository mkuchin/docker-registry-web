## Example configuration of docker registry, docker-registry-web and nginx

### How to run:

1. Copy this directory to your host
2. Generate private key and self signed certificate with script:
    
        ./generate-keys.sh
    
3. Start containers with docker-compose    
    
        docker-compose up
     
It will run docker registry on `localhost` and web ui on `http://localhost/`.
To enable access to registry from other hosts please change docker registry config property `auth.token.realm` to externally accessible url of `registry-web`.

### How to check if it working:
  
1. Login into `http://localhost/` with *admin/admin* username/password
2. Create test user and grant 'write-all' role to that user.
3. On the local shell:
         
         docker login localhost
         docker pull hello-world
         docker tag hello-world localhost/hello-world:latest
         docker push localhost/hello-world:latest
         docker rmi localhost/hello-world:latest
		 docker run localhost/hello-world:latest