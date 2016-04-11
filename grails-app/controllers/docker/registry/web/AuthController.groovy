package docker.registry.web

import java.text.SimpleDateFormat

class AuthController {

  def tokenService

  def index() {
    log.info("Auth params: $params")
    log.info("Auth headers")
    request.headerNames.each { name ->
      log.info("  ${name}: ${request.getHeader(name)}")
    }
    def auth = ''
    try {
      auth = request.getHeader('Authorization').split(' ')[1]
    } catch (e) {
      log.error "Error parsing auth header", e
    }
    log.info "Auth: $auth"
    //scope examples:
    //repository:hub/search_api:pull
    //registry:catalog:*
    //repository:hello-world:push,pull
    def scopeList = params.scope.split(':')
    def scope = [type: scopeList[0], name: scopeList[1], actions: scopeList[2].split(',')]
    def subject = params.account


    def dateFormat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss'Z'")
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"))
    def now = dateFormat.format(new Date())
    //access examples:
    // [[type: "registry", name:"catalog", actions:['*']]]
    // [[type: "repository", name: "hello-world", actions:["push", "pull"]]]
    def access = [scope]
    log.info "Request scope: $scope"
    log.info "Access list: ${access}"
    def token = tokenService.generate(subject, access)

    def json = [token: token, expires_in: 3600, issued_at: now]
    log.info "Auth response: $json"
    render(contentType: 'application/json') {
      json
    }
  }
}