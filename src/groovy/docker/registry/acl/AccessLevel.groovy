package docker.registry.acl

enum AccessLevel {
  NONE, PULL('pull'), PUSH('pull', 'push'), ADMIN('pull', 'push', '*'), UI_DELETE('ui-delete')

  AccessLevel(String... access) {
    this.actions = (access as List).asImmutable()
  }
  final List actions
}