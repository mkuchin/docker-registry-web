package docker.registry.acl

enum AccessLevel {
  NONE, PULL('pull'), PUSH('pull', 'push'), ADMIN('pull', 'push', '*')

  AccessLevel(String... access) {
    this.actions = (access as List).asImmutable()
  }
  final List actions
}