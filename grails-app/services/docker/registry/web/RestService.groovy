package docker.registry.web

import grails.plugins.rest.client.RequestCustomizer
import grails.plugins.rest.client.RestResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod

import javax.annotation.PostConstruct

class RestService {
  @Value('${registry.url}')
  String url

  @Value('${registry.basic_auth}')
  String basicAuth

  def tokenService
  def headers = [:]
  //v2 manifest header to get correct digest for docker 1.10
  def v2header = ['Accept': 'application/vnd.docker.distribution.manifest.v2+json']

  List generateAccess(String name, String action = 'pull', String type = 'repository') {
    [[type: type, name: name, actions: [action]]]
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
    request(HttpMethod.GET, "${url}/${path}" as String, v2 ? headers + v2header : headers, access)
  }

  def delete(String path, List access) {
    def res = request(HttpMethod.DELETE, "${url}/${path}", headers, access)
    log.info res.statusCode
    [deleted: res.statusCode.'2xxSuccessful', response: res]
  }

  @PostConstruct
  void init() {
    //set auth header if REGISTRY_BASIC_AUTH is set
    if (basicAuth) {
      log.info "Setting basic auth header: ${basicAuth}"
      headers['Authorization'] = "Basic ${basicAuth}"
    }
  }

  def request(HttpMethod method, String url, Map headers, List access = []) {
    def token = tokenService.generate('', access)?.token
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
