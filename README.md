# docker-registry-web

Web UI for private docker registry v2.

###How to run:
    docker run -i -t -p 8080:8080 -e REGISTRY_HOST=172.17.0.1 -e REGISTRY_PORT=5000 hyper/docker-registry-web
and open http://localhost:8080