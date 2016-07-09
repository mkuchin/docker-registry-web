package docker.registry.web

import docker.registry.AccessControl
import docker.registry.acl.AccessLevel
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(AuthService)
class AuthPermissionSpec extends Specification {

  void testDeny() {

    given:
    def acls = [new AccessControl(ip: '*', name: 'hello*', level: AccessLevel.PUSH),
                new AccessControl(ip: '*', name: '*hello*', level: AccessLevel.PUSH)]
    def ip = '127.0.0.1'
    expect:
    service.getScopePermissions(generateScope('ubuntu'), acls, ip) == []
    service.getScopePermissions(generateScope('ubuntu/hello'), acls, ip) == []
    service.getScopePermissions(generateScope('hello/ubuntu'), acls, ip) == []
  }

  void testDenyIp() {
    given:
    def acls = [new AccessControl(ip: '192.*', name: '**', level: AccessLevel.PULL),
                new AccessControl(ip: '192.168.*', name: 'hello/**', level: AccessLevel.PUSH)]
    expect:
    service.getScopePermissions(generateScope('hello/hello'), acls, '127.0.0.1') == []
    service.getScopePermissions(generateScope('hello'), acls, '192.0.0.1') == AccessLevel.PULL.actions
    service.getScopePermissions(generateScope('hello/hello'), acls, '192.0.0.1') == AccessLevel.PULL.actions
    service.getScopePermissions(generateScope('hello/hello'), acls, '192.168.0.1') == AccessLevel.PUSH.actions
  }

  void testScopeAllow() {
    given:
    def acls = [new AccessControl(ip: '*', name: 'hello*', level: AccessLevel.PUSH)]
    def ip = '127.0.0.1'
    expect:
    service.getScopePermissions(generateScope('hello'), acls, ip) == AccessLevel.PUSH.actions
    service.getScopePermissions(generateScope('hello-world-1'), acls, ip) == AccessLevel.PUSH.actions
    service.getScopePermissions(generateScope('hello.ubuntu'), acls, ip) == AccessLevel.PUSH.actions

  }

  void testMultipleAcls() {
    given:
    def acls = [new AccessControl(ip: '*', name: 'max/*', level: AccessLevel.PUSH),
                new AccessControl(ip: '*', name: 'general/*', level: AccessLevel.PULL),
                new AccessControl(ip: '*', name: '**', level: AccessLevel.NONE),
                new AccessControl(ip: '*', name: 'single_level', level: AccessLevel.PUSH),
                new AccessControl(ip: '*', name: '{max,test}/*', level: AccessLevel.PUSH)
    ]
    def ip = '127.0.0.1'
    expect:
    service.getScopePermissions(generateScope('max/ubuntu'), acls, ip) == AccessLevel.PUSH.actions
    service.getScopePermissions(generateScope('general/ubuntu'), acls, ip) == AccessLevel.PULL.actions
    service.getScopePermissions(generateScope('single_level'), acls, ip) == AccessLevel.PUSH.actions
    service.getScopePermissions(generateScope('single'), acls, ip) == []
    service.getScopePermissions(generateScope('test/test'), acls, ip) == AccessLevel.PUSH.actions
  }

  void testUiDelete() {
    given:
    def acls = [new AccessControl(ip: '*', name: 'max/*', level: AccessLevel.UI_DELETE)]
    def ip = '127.0.0.1'
    expect:
    service.getScopePermissions(generateScope('max/ubuntu'), acls, ip) == AccessLevel.UI_DELETE.actions
    service.getScopePermissions(generateScope('test/test'), acls, ip).empty
  }

  def generateScope(String name) {
    [type: 'repository', name: name, actions: ['push', 'pull']]
  }
}
