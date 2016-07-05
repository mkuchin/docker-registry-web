package docker.registry.web

import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Value

class RepositoryController {
  @Value('${registry.readonly}')
  boolean readonly
  int recordsPerPage = 100

  @Value('${registry.name}')
  String registryName

  def restService

  //{"Type":"registry","Name":"catalog","Action":"*"}
  def index() {
    def repoCount = []
    boolean pagination = false
    def next = null
    boolean hasNext = false
    def message
    def url = "_catalog?n=${recordsPerPage}"
    try {
      if (params.start) {
        url += "&last=${params.start}"
      }
      def restResponse = restService.get(url, restService.generateAccess('catalog', '*', 'registry'))
      if (!restResponse.statusCode.'2xxSuccessful') {
        def statusCode = restResponse.statusCode
        log.warn "URI: '$url' responseCode: ${statusCode}"
        message = "status=${statusCode} ${statusCode.name()} ${restResponse.text}"

      }
      hasNext = restResponse.headers.getFirst('Link') != null
      pagination = hasNext || params.prev != null
      def repos = restResponse.json.repositories
      next = repos ? repos.last() : null

      repoCount = repos.collect { name ->
        def tagsCount = getTagList(name).size()
        [name: name, tags: tagsCount]
      }
    } catch (e) {
      log.error "Can't access registry: $url", e
      message = e.message
    }
    [repos: repoCount, pagination: pagination, next: next, prev: params.start, hasNext: hasNext, registryName: registryName, message: message]
  }

  def tags() {
    def name = params.id.decodeURL()
    def tags = getTags(name)
    if (!tags.count { it.exists })
      redirect action: 'index'
    [tags: tags, readonly: readonly, registryName: registryName]
  }


  private def getTags(name) {
    def tags = getTagList(name).findAll { it }.collect { tag ->
      def manifest = restService.get("${name}/manifests/${tag}", restService.generateAccess(name))
      def exists = manifest.statusCode.'2xxSuccessful'
      def topLayer
      def size = 0
      def layers
      if (exists) {
        topLayer = new JsonSlurper().parseText(manifest.json.history.first().v1Compatibility)
        layers = getLayers(name, tag)
        size = layers.collect { it.value }.sum()
      }

      def createdStr = topLayer?.created
      def createdDate = DateConverter.convert(createdStr)
      long unixTime = createdDate?.time ?: 0

      [name: tag, count: layers?.size(), size: size, exists: exists, id: topLayer?.id?.substring(0, 11), created: createdDate, createdStr: createdStr, unixTime: unixTime]
    }
    tags
  }

  private def getLayers(String name, String tag) {
    def json = restService.get("${name}/manifests/${tag}", restService.generateAccess(name), true).json

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

  private List getTagList(name) {
    restService.get("${name}/tags/list", restService.generateAccess(name)).json?.tags ?: []
  }

  def tag() {
    def name = params.id.decodeURL()
    def tag = params.name
    def res = restService.get("${name}/manifests/${tag}", restService.generateAccess(name)).json
    def history = res.history.v1Compatibility.collect { jsonValue ->
      def json = new JsonSlurper().parseText(jsonValue)
      [id: json.id.substring(0, 11), cmd: (json.container_config.Cmd?.last() ?: '').replaceAll('&&', '&&\n')]
    }

    def blobs = res.fsLayers.collect { it.blobSum }
    def layers = getLayers(name, tag)
    history.eachWithIndex { entry, i ->
      def digest = blobs[i]
      entry.size = layers[digest] ?: 0
    }

    [history: history, totalSize: history.sum { it.size }, registryName: registryName]
  }

  def delete() {
    def name = params.name.decodeURL()
    def tag = params.id
    if (!readonly) {
      def manifest = restService.get("${name}/manifests/${tag}", restService.generateAccess(name, 'pull'), true)
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
      def result = restService.delete("${name}/manifests/${digest}", restService.generateAccess(name, '*'))
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
