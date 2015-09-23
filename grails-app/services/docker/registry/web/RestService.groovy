package docker.registry.web

import grails.plugins.rest.client.RestBuilder
import org.springframework.beans.factory.annotation.Value

class RestService {
  @Value('${registry.host}')
  String host

  @Value('${registry.port}')
  String port

  String registryUrl

  Closure requestCustomizer

  def check(String url) {
    def rest = new RestBuilder()
    try {
      def status = rest.get(url, requestCustomizer).status
      log.info "HTTP status: $status"
      return status == 200
    } catch (e) {
      log.warn e
      return false
    }
  }

  def get(String path) {
    def rest = new RestBuilder()
    rest.get("${registryUrl}/${path}" as String, requestCustomizer)
  }

  def headLength(String path) {
    def rest = new RestBuilder()
    def res = rest.head("${registryUrl}/${path}", requestCustomizer)
    def size = res.responseEntity.headers.getFirst('Content-Length')
    size as BigInteger
  }

  def delete(String path) {
    def rest = new RestBuilder()
    def res = rest.delete("${registryUrl}/${path}", requestCustomizer)
    log.info res.statusCode
  }

  void init() {
    //set requestCustomizer if REGISTRY_AUTH is set
    String registryAuth = System.env.REGISTRY_AUTH
    if (registryAuth) {
      log.info "Setting auth token: $registryAuth"
      requestCustomizer = {
        auth("Basic ${registryAuth}")
      }
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
}
