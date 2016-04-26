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

    def scope = [:]
    log.info "Scope: ${params.scope}"
    if (params.scope) {
      def scopeList = params.scope.split(':')
      scope = [type: scopeList[0], name: scopeList[1], actions: scopeList[2].split(',')]
    }
    def subject = params.account

    //access examples:
    // [[type: "registry", name:"catalog", actions:['*']]]
    // [[type: "repository", name: "hello-world", actions:["push", "pull"]]]
    if (authResult.valid) {
      def aclList = authResult.acls
      log.info "Requested scope: $scope"
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
      log.info "actions = ${actions}"
      scope.actions = actions

      def access = [scope]
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