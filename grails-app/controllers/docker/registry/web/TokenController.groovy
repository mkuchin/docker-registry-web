package docker.registry.web

class TokenController {

  def index() {
    [list: AuthToken.list()]
  }

  def show() {
    def token = AuthToken.get(params.id)
    [token: token, repos: RepositoryToken.findAllByToken(token)]
  }

  def add() {

  }
}
