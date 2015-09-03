package docker.registry.web

import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Value

class RepositoryController {
  @Value('${registry.url}')
  String registryUrl

  def restService

  def index() {
    def repos = restService.get('_catalog').repositories
    [repos: repos]
  }

  def tags() {

    def name = params.id
    def resp = restService.get("${name}/tags/list")
    def tags = resp.tags.collect { tag ->
      def manifest = restService.get("${name}/manifests/${tag}")

      def size = manifest.fsLayers.sum { layer ->
        def digest = layer.blobSum
        restService.headLength("${name}/blobs/${digest}") ?: 0
      }
      [name: tag, data: manifest, size: size]
    }
    [tags: tags]
  }

  def tag() {
    def res = restService.get("${params.name}/manifests/${params.id}")
    def history = res.history.v1Compatibility.collect { jsonValue ->
      def json = new JsonSlurper().parseText(jsonValue)
      //log.info json as JSON
      json
    }
    [history: history]
  }
}
