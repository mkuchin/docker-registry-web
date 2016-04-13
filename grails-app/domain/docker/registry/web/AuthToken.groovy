package docker.registry.web

class AuthToken {
  String name
  String password
  boolean read
  boolean write
  boolean delete
  static constraints = {
  }
}
