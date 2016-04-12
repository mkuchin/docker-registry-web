package docker.registry.web

import grails.plugins.rest.client.RequestCustomizer
import grails.plugins.rest.client.RestResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

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

  @Cacheable(value = "blobs", key = "#name + '/' + #digest")
  BigInteger getBlobSize(String name, String digest) {
    headLength("${name}/blobs/${digest}") ?: 0
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

  def get(String path, boolean v2 = false) {
    request(HttpMethod.GET, "${registryUrl}/${path}" as String, v2 ? headers : headers + v2header)
  }

  def headLength(String path) {
    def res = request(HttpMethod.HEAD, "${registryUrl}/${path}", headers)
    def size = res.responseEntity.headers.getFirst('Content-Length')
    size as BigInteger
  }

  def delete(String path) {
    def res = request(HttpMethod.DELETE, "${registryUrl}/${path}", headers)
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

  def request(HttpMethod method, String url, Map headers) {
    RestResponse result = requestInternal(headers, method, url)
    if (result.statusCode == HttpStatus.UNAUTHORIZED) {
      def json = result.json
      log.info "Auth requested: $json"
      //{"errors":[{"code":"UNAUTHORIZED","message":"authentication required","detail":[{"Type":"registry","Name":"catalog","Action":"*"}]}]}

      //default access
      def access = [[type: "registry", name: "catalog", actions: ['*']]]

      def detail = json.errors[0].detail
      log.info "Detail: $detail"
      if (detail) {
        String type = detail.Type
        String name = detail.Name
        String action = detail.Action
        List actions = action.split(',')
        access = [[type: type, name: name, actions: actions]]
      }

      log.info "Access: $access"
      String token = tokenService.generate('root', access).token
      log.info "Token generated: $token"
      headers['Authorization'] = "Bearer $token"
      result = requestInternal(headers, method, url)
      log.info "Response status after auth: $result.statusCode"
    }
    return result
  }

  private RestResponse requestInternal(Map headers, HttpMethod method, String url) {
    def customizer = new RequestCustomizer()
    headers.each { k, v -> customizer.header(k, v) }
    def result = new CustomRestBuilder().request(method, url, customizer)
    result
  }
}
