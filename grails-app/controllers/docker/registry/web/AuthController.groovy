package docker.registry.web

class AuthController {
  def index() {
    log.info("Auth params: $params")
    log.info("Auth headers")
    request.headerNames.each { name ->
      log.info("  ${name}: ${request.getHeader(name)}")
    }
    render(contentType: 'application/json') {
      ok()
    }
  }
}
