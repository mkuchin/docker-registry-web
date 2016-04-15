package docker.registry

import docker.registry.acl.AccessLevel
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

//Access control for role
@EqualsAndHashCode(includes = 'id')
@ToString(includes = 'ip,name,level', includeNames = true, includePackage = false)
class AccessControl {
  String ip
  String name
  AccessLevel level
  static constraints = {
  }
}
