package docker.registry.api

import docker.registry.Event
import docker.registry.web.DateConverter

class NotificationController {

  /*
  //push request
  {
    "events": [
      {
        "id": "e08700d8-d50e-4eee-9a20-28903de002cf",
        "timestamp": "2016-04-21T02:22:40.133007424Z",
        "action": "push",
        "target": {
        "mediaType": "application/vnd.docker.distribution.manifest.v2+json",
        "size": 713,
        "digest": "sha256:70c8519714a97ec5496c38c108af94441e9eaff4702d13cdef9452280499eed0",
        "length": 713,
        "repository": "hello-world",
        "url": "https://hub.devspire.com.au/v2/hello-world/manifests/sha256:70c8519714a97ec5496c38c108af94441e9eaff4702d13cdef9452280499eed0",
        "tag": "v17"
      },
        "request": {
        "id": "8e8f84f3-ab22-4142-be6b-6941d50aa7f0",
        "addr": "203.213.76.106",
        "host": "hub.devspire.com.au",
        "method": "PUT",
        "useragent": "docker/1.11.0 go/go1.5.4 git-commit/4dc5990 kernel/4.1.19-boot2docker os/linux arch/amd64 UpstreamClient(Docker-Client/1.11.0 \\(darwin\\))"
      },
        "actor": {
        "name": "max"
      },
        "source": {
        "addr": "27d37c02d838:5000",
        "instanceID": "f7d866ec-5b33-4623-b769-947303c4355e"
      }
      }
  ]
  }
    //pull req
    {
   "events": [
      {
         "id": "6d94020f-7377-428d-8e9a-8c90acbe35c1",
         "timestamp": "2016-04-21T02:29:59.343450907Z",
         "action": "pull",
         "target": {
            "mediaType": "application/vnd.docker.distribution.manifest.v2+json",
            "size": 713,
            "digest": "sha256:9c6e22c605c63e8bba797c93d226302f659c2164ba0c600a9d068ce4239a4d91",
            "length": 713,
            "repository": "hello-world",
            "url": "https://hub.devspire.com.au/v2/hello-world/manifests/sha256:9c6e22c605c63e8bba797c93d226302f659c2164ba0c600a9d068ce4239a4d91",
            "tag": "v19"
         },
         "request": {
            "id": "1b99c8fe-2de1-437a-874c-e640ca3e8de3",
            "addr": "203.213.76.106",
            "host": "hub.devspire.com.au",
            "method": "GET",
            "useragent": "docker/1.11.0 go/go1.5.4 git-commit/4dc5990 kernel/4.1.19-boot2docker os/linux arch/amd64 UpstreamClient(Docker-Client/1.11.0 \\(darwin\\))"
         },
         "actor": {
            "name": "max"
         },
         "source": {
            "addr": "27d37c02d838:5000",
            "instanceID": "f7d866ec-5b33-4623-b769-947303c4355e"
         }
      }
   ]
  }
  */

  def index() {
    def json = request.JSON
    if (!json) {
      log.warn "No events received, json: $json"
    }

    json.events.each { event ->
      def target = event.target
      //skip system notifications and binary events
      if (target.mediaType == 'application/vnd.docker.distribution.manifest.v2+json' && event.actor.name) {
        try {
          def eventMap = [repo: target.repository, tag: target.tag, action: event.action,
                          user: event.actor.name, ip: event.request.addr.split(':')[0], time: event.timestamp]
          log.info eventMap
          eventMap.time = DateConverter.convert(eventMap.time)
          new Event(event).save(failOnError: true)
        } catch (e) {
          log.warn "Error processing json: $json", e
        }
      }
    }
    render text: 'ok'
  }
}
