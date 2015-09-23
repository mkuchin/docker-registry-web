package docker.registry.web

import grails.plugins.rest.client.RestBuilder
import org.springframework.beans.factory.annotation.Value

class RestService {
  @Value('${registry.host}')
  String host

  @Value('${registry.port}')
  String port

  String registryUrl
  @Value('${registry.auth}')
  String registryAuth

  def check(def url) {
    def rest = new RestBuilder()
    try {
      def status = rest.get(url).status
      return status == 200
    } catch (e) {
      log.warn e
      return false
    }
  }

  def get(String path) {
    def rest = new RestBuilder()
    rest.get("${registryUrl}/${path}"){
    auth "Basic ${registryAuth}"
    }
  }

  def headLength(String path) {
    def rest = new RestBuilder()
    def res = rest.head("${registryUrl}/${path}")
    def size = res.responseEntity.headers.getFirst('Content-Length')
    size as BigInteger
  }

  def delete(String path) {
    def rest = new RestBuilder()
    def res = rest.delete("${registryUrl}/${path}")
    log.info res.statusCode
  }

  void init() {
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
