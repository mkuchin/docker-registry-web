package docker.registry.web

import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Value

class RepositoryController {
  @Value('${registry.url}')
  String registryUrl

  def restService

  def index() {
    def repos = restService.get('_catalog').json.repositories.collect { name ->
      [name: name, tags: getTags(name).count { it.found }]
    }
    [repos: repos]
  }

  def tags() {

    def name = params.id
    def tags = getTags(name)
    [tags: tags]
  }

  private def getTags(name) {
    def resp = restService.get("${name}/tags/list").json
    def tags = resp.tags.collect { tag ->
      def manifest = restService.get("${name}/manifests/${tag}")
      def responseCode = manifest.statusCode

      def size = manifest.json.fsLayers?.sum { layer ->
        def digest = layer.blobSum
        restService.headLength("${name}/blobs/${digest}") ?: 0
      }
      [name: tag, data: manifest.json, size: size, found: responseCode.'2xxSuccessful']
    }
    tags
  }

  def tag() {
    def res = restService.get("${params.name}/manifests/${params.id}").json
    def history = res.history.v1Compatibility.collect { jsonValue ->
      def json = new JsonSlurper().parseText(jsonValue)
      //log.info json as JSON
      json
    }
    [history: history]
  }

  def delete() {
    def name = params.name
    def tag = params.id
    def manifest = restService.get("${name}/manifests/${tag}")
    def digest = manifest.responseEntity.headers.getFirst('Docker-Content-Digest')
    log.info digest
    /*
    def blobSums = manifest.json.fsLayers?.blobSum
    blobSums.each { digest ->
      log.info "Deleting blob: ${digest}"
      restService.delete("${name}/blobs/${digest}")
    }
    */
    log.info "Deleting manifest"
    restService.delete("${name}/manifests/${digest}")

    redirect action: 'tags', id: name
  }
}
