package docker.registry

import groovy.transform.ToString

@ToString(includeNames = true, includePackage = false)
class Event {
  String ip
  String username
  String repo
  String action
  String tag
  Date time
  static constraints = {
    version: false
  }
}
