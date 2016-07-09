package docker.registry.web

import docker.registry.AccessControl
import docker.registry.Role
import docker.registry.RoleAccess
import docker.registry.User
import docker.registry.acl.AccessLevel
import docker.registry.acl.AuthResult
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

import javax.servlet.http.HttpServletRequest

class AuthService {
  AuthenticationManager authenticationManager
  def springSecurityService

  @Value('${registry.auth.enabled}')
  boolean authEnabled

  AuthResult login(HttpServletRequest request) {
    def authResult = new AuthResult()
    try {

      def header = request.getHeader('Authorization')
      if (header) {
        def auth = header.split(' ')[1]
        //log.info "Auth: $auth"
        def userPass = new String(auth.decodeBase64()).split(':')
        authResult = login(userPass[0], userPass[1])
      } else {
        log.info "Anonymous access requested"
        return new AuthResult()
      }
    } catch (e) {
      log.warn "Access denied: ${e.message}"
    }
    authResult
  }

  AuthResult login(String username, String password) {
    def authToken = new UsernamePasswordAuthenticationToken(username, password)
    log.info "authenticating user: $username"
    def authResult = authenticationManager.authenticate(authToken)
    log.info authResult
    log.info "Granted roles: ${authResult.authorities}"
    //could be ROLE_NO_ROLES if no roles found
    def roles = authResult.authorities.collect { role ->
      def roleName = role.authority
      Role.findByAuthority(roleName)
    }.findAll { it }
    def acls = roles.collect { role ->
      RoleAccess.findAllByRole(role).acl
    }.flatten()
    new AuthResult(roles, acls)
  }

  List getScopePermissions(Map scope, Collection aclList, String ip) {
    def actions = []
    def typeValid = scope.type == 'repository'
    if (aclList && scope && typeValid) {
      //todo: catalog role for type=catalog request
      log.info "checking acls: $aclList"
      String name = scope.name

      log.info("Repo name=${name}, ip=${ip}")
      //check acls
      actions = aclList.collect { AccessControl acl ->
        if (GlobMatcher.check(acl.name, name) && GlobMatcher.check(acl.ip, ip))
          return acl.level
        else
          return AccessLevel.NONE
      }.actions.flatten().unique()
      log.info "Granting permissions: $actions"
    }
    actions
  }

  List getCurrentUserPermissions(String name) {
    User user = springSecurityService.currentUser
    log.info "Checking current user permissions for user=${user.username}, repo=${name}"
    def aclList = user.authorities.acls.flatten()
    getScopePermissions([type: 'repository', name: name], aclList, 'local')
  }

  //returns true if delete allowed to current user
  boolean checkLocalDeletePermissions(String name) {
    if (authEnabled)
      return getCurrentUserPermissions(name).contains('ui-delete')
    else
      return true
  }
}
