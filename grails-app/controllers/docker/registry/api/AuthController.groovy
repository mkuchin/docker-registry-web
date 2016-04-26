package docker.registry.api

import docker.registry.AccessControl
import docker.registry.acl.AccessLevel

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
    log.info "Translates scope list: $scopeList"
    def subject = params.account

    //access examples:
    // [[type: "registry", name:"catalog", actions:['*']]]
    // [[type: "repository", name: "hello-world", actions:["push", "pull"]]]
    if (authResult.valid) {
      def access = []
      def aclList = authResult.acls
      scopeList.collect { scope ->
        log.info "Requested scope: $scope"
        List actions = getScopePermissions(scope, aclList)
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

  private List getScopePermissions(Map scope, List aclList) {
    def actions = []
    def typeValid = scope.type == 'repository'
    if (aclList && scope && typeValid) {
      //todo: catalog role for type=catalog request
      log.info "checking acls: $aclList"
      String name = scope.name
      String ip = request.getRemoteAddr()
      log.info("Repo name=${name}, ip=${ip}")
      //check acls
      def level = aclList.collect { AccessControl acl ->
        if (name.startsWith(acl.name) && ip.startsWith(acl.ip))
          return acl.level
        else
          return AccessLevel.NONE
      }.max()
      log.info "Granting permission: $level"
      actions = level.actions
    }
    actions
  }
  //todo tests:
  /*
     Scope: null -> Access list: [ [actions:[]] ]
     Scope: [type:repository, name:hello-world-1, actions:[push, pull]] -> Access list: [[type:repository, name:hello-world-1, actions:[pull, push]]]
     Scope: [[type:repository, name:hello-world-1, actions:[push, pull]], [type:repository, name:hello-world-2, actions:[pull]]] -> ?
   */
}