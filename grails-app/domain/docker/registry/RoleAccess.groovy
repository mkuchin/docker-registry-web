package docker.registry

class RoleAccess {
  Role role
  AccessControl acl

  static RoleAccess create(Role role, AccessControl acl, boolean flush = false) {
    new RoleAccess(role: role, acl: acl).save(flush: flush, insert: true)
  }
  static constraints = {
  }
}
