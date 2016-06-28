package docker.registry

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode(includes = 'authority')
@ToString(includes = 'authority', includeNames = true, includePackage = false)
class Role implements Serializable {

  private static final long serialVersionUID = 1
  private static specialRoles = ['UI_USER', 'UI_ADMIN'] as Set

  String authority

  boolean isSpecialRole() {
    specialRoles.contains(authority)
  }

  String getCode() {
    "role.${authority}"
  }

  Role(String authority) {
    this()
    this.authority = authority
  }

  List<AccessControl> getAcls() {
    RoleAccess.findAllByRole(this).acl
  }

  static constraints = {
    authority blank: false, unique: true
  }

  static mapping = {
    cache true
  }
}
