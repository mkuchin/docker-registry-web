package docker.registry.web

import grails.plugin.cache.Cacheable
import grails.plugins.rest.client.RequestCustomizer
import grails.plugins.rest.client.RestResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod

class RestService {
  @Value('${registry.host}')
  String host

  @Value('${registry.port}')
  String port

  String registryUrl

  def tokenService
  def headers = [:]
  //v2 manifest header to get correct digest for docker 1.10
  def v2header = ['Accept': 'application/vnd.docker.distribution.manifest.v2+json']

  List generateAccess(String name, String action = 'pull', String type = 'repository') {
    [[type: type, name: name, actions: [action]]]
  }

  @Cacheable(value = "blobs", key = "#name + '/' + #digest")
  BigInteger getBlobSize(String name, String digest) {
    headLength("${name}/blobs/${digest}", generateAccess(name)) ?: 0
  }

  def check(String url) {
    try {
      def status = request(HttpMethod.GET, url, headers).status
      log.info "HTTP status: $status"
      return status == 200
    } catch (e) {
      log.warn e
      return false
    }
  }

  def get(String path, List access = [], boolean v2 = false) {
    request(HttpMethod.GET, "${registryUrl}/${path}" as String, v2 ? headers + v2header : headers, access)
  }

  def headLength(String path, List access) {
    def res = request(HttpMethod.HEAD, "${registryUrl}/${path}", headers, access)
    def size = res.responseEntity.headers.getFirst('Content-Length')
    size as BigInteger
  }

  def delete(String path, List access) {
    def res = request(HttpMethod.DELETE, "${registryUrl}/${path}", headers, access)
    log.info res.statusCode
  }

  void init() {
    //set requestCustomizer if REGISTRY_AUTH is set
    String registryAuth = System.env.REGISTRY_AUTH
    if (registryAuth) {
      log.info "Setting auth header: $registryAuth"
      headers['auth'] = "Basic ${registryAuth}"
    }

    //auto detect registry protocol
    def protoList = ['http', 'https']
    for (proto in protoList) {
      def url = "$proto://${host}:${port}/v2"
      log.info "Trying to connect $url"
      if (check(url)) {
        registryUrl = url
        break
      }
    }
    if (!registryUrl)
      throw new RuntimeException("Can't connect to registry")

    log.info "Registry URL detected: $registryUrl"
  }

  def request(HttpMethod method, String url, Map headers, List access = []) {
    def token = tokenService.generate('root', access).token
    def authHeaders = token ? ['Authorization': "Bearer $token"] : [:]
    RestResponse result = requestInternal(headers + authHeaders, method, url)
    return result
  }

  private RestResponse requestInternal(Map headers, HttpMethod method, String url) {
    def customizer = new RequestCustomizer()
    headers.each { k, v -> customizer.header(k, v) }
    def result = new CustomRestBuilder().request(method, url, customizer)
    result
  }
}
