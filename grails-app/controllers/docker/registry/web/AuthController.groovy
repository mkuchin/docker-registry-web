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
    def scopeList = params.scope.split(':')
    def scope = [type: scopeList[0], name: scopeList[1], action: scopeList[2]]
    def subject = params.account


    def dateFormat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss'Z'")
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"))
    def now = dateFormat.format(new Date())
    //access examples:
    // [[type: "registry", name:"catalog", actions:['*']]]
    // [[type: "repository", name: "hello-world", actions:["push", "pull"]]]
    def access = [[type: scope.type, name: scope.name, actions: [scope.action]]]
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