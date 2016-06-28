## Example configuration of docker registry, docker-registry-web and nginx

Docker registry and registry-web will be available on localhost on port 80.

git clone -b auth https://github.com/mkuchin/docker-registry-web.git
cd docker-registry-web/examples/nginx-auth-enabled/
docker-compose up

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