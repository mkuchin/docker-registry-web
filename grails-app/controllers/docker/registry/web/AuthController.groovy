package docker.registry.web

class AuthController {

  def tokenService

  def index() {
    log.info("Auth params: $params")
    def auth = ''
    try {
      auth = request.getHeader('Authorization').split(' ')[1]
    } catch (e) {
      log.error "Error parsing auth header", e
    }
    log.info "Auth: $auth"
    def service = params.service
    //scope examples:
    //repository:hub/search_api:pull
    //registry:catalog:*
    //repository:hello-world:push,pull
    //repository:docker-registry-web:* - delete request

    def scopeList = params.scope.split(':')
    def scope = [type: scopeList[0], name: scopeList[1], actions: scopeList[2].split(',')]
    def subject = params.account



    //access examples:
    // [[type: "registry", name:"catalog", actions:['*']]]
    // [[type: "repository", name: "hello-world", actions:["push", "pull"]]]
    def access = [scope]
    log.info "Request scope: $scope"
    log.info "Access list: ${access}"
    def tokenJson = tokenService.generate(subject, access)

    log.info "Auth response: $tokenJson"
    render(contentType: 'application/json') {
      tokenJson
    }
  }
}