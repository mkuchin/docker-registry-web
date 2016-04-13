package docker.registry.web

class AuthController {

  def tokenService

  def index() {
    log.info("Auth params: $params")
    def auth = ''
    //todo: anonymous users, no auth
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

    //empty scope for login/ping
    def scope = []
    if (params.scope) {
      def scopeList = params.scope.split(':')
      scope = [type: scopeList[0], name: scopeList[1], actions: scopeList[2].split(',')]
    }
    def subject = params.account

    boolean valid = false
    def token
    if (auth) {
      def credentials = new String(auth.decodeBase64()).split(':')
      if (credentials.size() == 3 && credentials[0] == 'token') {
        def id = credentials[1] as long
        def password = credentials[2]
        log.info("Found token id=${id}")
        token = AuthToken.get(id)
        if (token) {
          def hash = password.encodeAsPassword()
          valid = hash == token.password
        }
        log.info("Password valid: $valid")
        if (!valid)
          log.info "Expected ${password}, recieved ${hash}"
      }
    }

    //access examples:
    // [[type: "registry", name:"catalog", actions:['*']]]
    // [[type: "repository", name: "hello-world", actions:["push", "pull"]]]
    if (valid) {
      log.info "Requested scope: $scope"
      def actions = []
      if (scope) {
        def typeValid = scope.type == 'repository'
        def repository = Repository.findByName(scope.name)
        if (typeValid && repository) {
          def repoToken = RepositoryToken.findByRepositoryAndToken(repository, token)
          if (repoToken) {
            log.info "Granting permission: r=${token.read}, w=${token.write}"
            if (token.read)
              actions.add('pull')
            if (token.write)
              actions.add('push')
          }
        }
        log.info "actions = ${actions}"
        scope.actions = actions
      }
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