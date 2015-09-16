package docker.registry.web

import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Value

class RepositoryController {
  @Value('${registry.readonly}')
  boolean readonly


  def restService

  def index() {
    def repos = restService.get('_catalog').json.repositories.collect { name ->
      def tags = getTags(name, false)
      [name: name, tags: tags.count { it.exists }]
    }
    [repos: repos]
  }

  def tags() {
    def name = params.id.decodeURL()
    def tags = getTags(name, true)
    if (!tags.count { it.exists })
      redirect action: 'index'
    [tags: tags]
  }

  private def getTags(name, boolean deep = true) {
    def resp = restService.get("${name}/tags/list").json
    def tags = resp.tags.collect { tag ->
      def manifest = restService.get("${name}/manifests/${tag}")
      def exists = manifest.statusCode.'2xxSuccessful'
      BigInteger size = 0
      def topLayer
      if (deep && exists) {
        topLayer = new JsonSlurper().parseText(manifest.json.history.first().v1Compatibility)
        size = manifest.json.fsLayers.sum { layer ->
          def digest = layer.blobSum
          restService.headLength("${name}/blobs/${digest}") ?: 0
        }
      }
      [name: tag, data: manifest.json, size: size, exists: exists, id: topLayer?.id?.substring(0, 11)]
    }
    tags
  }

  def tag() {
    def name = params.id.decodeURL()
    def res = restService.get("${name}/manifests/${params.name}").json
    def history = res.history.v1Compatibility.collect { jsonValue ->
      def json = new JsonSlurper().parseText(jsonValue)
      //log.info json as JSON
      json
    }
    [history: history, totalSize: history.sum { it.Size as BigInteger }]
  }

  def delete() {
    def name = params.name.decodeURL()
    def tag = params.id
    if (!readonly) {
      def manifest = restService.get("${name}/manifests/${tag}")
      def digest = manifest.responseEntity.headers.getFirst('Docker-Content-Digest')
      log.info "Manifest digest: $digest"
      /*
    def blobSums = manifest.json.fsLayers?.blobSum
    blobSums.each { digest ->
      log.info "Deleting blob: ${digest}"
      restService.delete("${name}/blobs/${digest}")
    }
    */
      log.info "Deleting manifest"
      restService.delete("${name}/manifests/${digest}")
    } else
      log.warn 'Readonly mode!'
    redirect action: 'tags', id: name
  }
}
