package docker.registry.web

import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Value

class RepositoryController {
  @Value('${registry.readonly}')
  boolean readonly


  def restService

  //{"Type":"registry","Name":"catalog","Action":"*"}
  def index() {
    def repos = restService.get('_catalog').json.repositories.collect { name ->
      def tagsCount = getTagCount(name)
      [name: name, tags: tagsCount ]
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

  private def getTagCount(name) {
    def resp = restService.get("${name}/tags/list").json
    def tagsCount = 0
    try {
        tagsCount = resp.tags.size()
    } catch(e) {}
    tagsCount
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
          restService.getBlobSize(name, digest)
        }
      }

      // docker uses ISO8601 dates w/ fractional seconds (i.e. yyyy-MM-ddTHH:mm:ss.ssssssssZ),
      // which seem to confuse the Date parser, so truncate the timestamp and always assume UTC tz.
      def createdStr = topLayer?.created?.substring(0,19)
      def createdDate
      if (createdStr) {
        createdDate = Date.parse("yyyy-MM-dd'T'HH:mm:ss", createdStr)
      }

      [name: tag, data: manifest.json, size: size, exists: exists, id: topLayer?.id?.substring(0, 11), created: createdDate, createdStr: createdStr]
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

    def blobs = res.fsLayers.collect { it.blobSum }

    history.eachWithIndex { entry, i ->
      entry.size = entry.Size ?: restService.getBlobSize(name, blobs[i])
    }
    [history: history, totalSize: history.sum { it.size }]
  }

  def delete() {
    def name = params.name.decodeURL()
    def tag = params.id
    if (!readonly) {
      def manifest = restService.get("${name}/manifests/${tag}", true)
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
      //todo: show error/success
    } else
      log.warn 'Readonly mode!'
    redirect action: 'tags', id: params.name
  }
}
