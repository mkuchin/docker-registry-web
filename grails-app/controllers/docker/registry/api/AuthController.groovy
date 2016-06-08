package docker.registry.api

class AuthController {

  def tokenService
  def authService

  def index() {
    //log.info("Auth params: $params")

    //todo: anonymous users, no auth
    def authResult = authService.login(request)
    def service = params.service

    //scope examples:
    //repository:hub/search_api:pull
    //registry:catalog:*
    //repository:hello-world:push,pull
    //repository:docker-registry-web:* - delete request
    //empty scope for login/ping

    //could by multiple scopes
    ///scope=repository%3Ahello-world-2%3Apush%2Cpull&
    // scope=repository%3Ahello-world-1%3Apull
    log.info "Scope: ${params.scope}"
    def scopeList = params.list('scope').collect { scope ->
      def list = scope.split(':')
      [type: list[0], name: list[1], actions: list[2].split(',')]
    }
    log.info "Translated scope list: $scopeList"
    def subject = params.account

    //access examples:
    // [[type: "registry", name:"catalog", actions:['*']]]
    // [[type: "repository", name: "hello-world", actions:["push", "pull"]]]
    if (authResult.valid) {
      def access = []
      def aclList = authResult.acls
      scopeList.collect { scope ->
        log.info "Requested scope: $scope"
        String ip = request.getRemoteAddr()
        List actions = authService.getScopePermissions(scope, aclList, ip)
        scope.actions = actions
        log.info "Granted scope: ${actions}"
        access << scope
      }

      log.info "Access list: ${access}"
      def tokenJson = tokenService.generate(subject, access)

      log.info "Auth response: $tokenJson"
      render(contentType: 'application/json') {
        tokenJson
      }
    } else {
      render(status: 401, text: 'No auth')
    }
  }
}