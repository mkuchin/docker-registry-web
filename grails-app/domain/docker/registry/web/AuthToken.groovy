package docker.registry.web

class AuthToken {
  String name
  String password
  boolean read
  boolean write
  boolean delete
  static constraints = {
  }

  def getPermissions() {
    def list = []
    if (read)
      list.add('pull')
    if (write)
      list.add('push')
    if (delete)
      list.add('*')
    list.join(', ')
  }
}
