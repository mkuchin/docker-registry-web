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
  Closure v2header

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

  def get(String path, boolean v2 = false) {
    def rest = new RestBuilder()

    def customizer = requestCustomizer
    if (v2)
      customizer >>= v2header

    rest.get("${registryUrl}/${path}" as String, customizer)
  }

  def delete(String path) {
    def rest = new RestBuilder()
    def res = rest.delete("${registryUrl}/${path}", requestCustomizer)
    log.info res.statusCode
  }

  void init() {
    //v2 manifest header to get correct digest for docker 1.10
    v2header = { header 'Accept', 'application/vnd.docker.distribution.manifest.v2+json' }

    //set requestCustomizer if REGISTRY_AUTH is set
    String registryAuth = System.env.REGISTRY_AUTH
    if (registryAuth) {
      log.info "Setting auth token: $registryAuth"
      requestCustomizer = {
        auth("Basic ${registryAuth}")
      }
    } else
      requestCustomizer = {}

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
