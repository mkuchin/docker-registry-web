package docker.registry.web

import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Value

class RepositoryController {
  @Value('${registry.readonly}')
  boolean readonly
  int recordsPerPage = 100

  def restService

  def index() {
    def url = "_catalog?n=${recordsPerPage}"
    if (params.start) {
      url += "&last=${params.start}"
    }
    def restResponse = restService.get(url)

    boolean hasNext = restResponse.headers.getFirst('Link') != null
    def pagination = hasNext || params.prev != null
    def repos = restResponse.json.repositories
    def next = repos ? repos.last() : null

    def repoCount = repos.collect { name ->
      def tagsCount = getTagCount(name)
      [name: name, tags: tagsCount]
    }
    [repos: repoCount, pagination: pagination, next: next, prev: params.start, hasNext: hasNext]
  }

  def tags() {
    def name = params.id.decodeURL()
    def tags = getTags(name)
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


  private def getTags(name) {
    def resp = restService.get("${name}/tags/list").json
    def tags = resp.tags.findAll { it }.collect { tag ->

      def manifest = restService.get("${name}/manifests/${tag}")
      def exists = manifest.statusCode.'2xxSuccessful'
      def topLayer
      def size = 0
      def layers
      if (exists) {
        topLayer = new JsonSlurper().parseText(manifest.json.history.first().v1Compatibility)
        layers = getLayers(name, tag)
        size = layers.collect { it.value }.sum()
      }

      // docker uses ISO8601 dates w/ fractional seconds (i.e. yyyy-MM-ddTHH:mm:ss.ssssssssZ),
      // which seem to confuse the Date parser, so truncate the timestamp and always assume UTC tz.
      def createdStr = topLayer?.created?.substring(0,19)
      def createdDate
      long unixTime = 0
      if (createdStr) {
        createdDate = Date.parse("yyyy-MM-dd'T'HH:mm:ss", createdStr)
        unixTime = createdDate.time
      }

      [name: tag, count: layers?.size(), size: size, exists: exists, id: topLayer?.id?.substring(0, 11), created: createdDate, createdStr: createdStr, unixTime: unixTime]
    }
    tags
  }

  private def getLayers(String name, String tag) {
    def json = restService.get("${name}/manifests/${tag}", true).json

    if (json.schemaVersion == 2)
      return json.layers.collectEntries { [it.digest, it.size as BigInteger] }
    else {
      //fallback to manifest schema v1
      def history = json.history.v1Compatibility.collect { jsonValue ->
        new JsonSlurper().parseText(jsonValue)
      }

      def digests = json.fsLayers.collect { it.blobSum }
      history.eachWithIndex { entry, i ->
        entry.digest = digests[i]
        entry.size = entry.Size ?: 0
      }

      return history.collectEntries {
        [it.digest, it.size as BigInteger]
      }
    }

  }

  def tag() {
    def name = params.id.decodeURL()

    def tag = params.name
    def res = restService.get("${name}/manifests/${tag}").json
    def history = res.history.v1Compatibility.collect { jsonValue ->
      def json = new JsonSlurper().parseText(jsonValue)
      [id: json.id.substring(0, 11), cmd: json.container_config.Cmd.last().replaceAll('&&', '&&\n')]
    }

    def blobs = res.fsLayers.collect { it.blobSum }
    def layers = getLayers(name, tag)
    history.eachWithIndex { entry, i ->
      def digest = blobs[i]
      entry.size = layers[digest] ?: 0
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
      def result = restService.delete("${name}/manifests/${digest}")
      if (!result.deleted) {
        def text = ''
        try {
          boolean unsupported = result.response.json.errors[0].code == 'UNSUPPORTED'
          text = unsupported ? "Deletion disabled in registry, <a href='https://docs.docker.com/registry/configuration/#delete'>more info</a>." : result.text
        } catch (e) {
          text = result.text
        }
        flash.message = "Error deleting ${name}:${tag}: ${text}"
      } else {
        flash.message = "Tag ${name}:${tag} has been deleted"
        flash.success = true
      }
    } else {
      log.warn 'Readonly mode!'
      flash.message = "Readonly mode!"
    }
    flash.deleteAction = true
    redirect action: 'tags', id: params.name
  }
}
