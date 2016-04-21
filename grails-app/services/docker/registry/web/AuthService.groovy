package docker.registry.web

import docker.registry.Role
import docker.registry.RoleAccess
import docker.registry.acl.AuthResult
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

import javax.servlet.http.HttpServletRequest

class AuthService {
  AuthenticationManager authenticationManager

  AuthResult login(HttpServletRequest request) {
    def authResult = new AuthResult()
    try {
      def auth = request.getHeader('Authorization').split(' ')[1]
      //log.info "Auth: $auth"
      def userPass = new String(auth.decodeBase64()).split(':')
      authResult = login(userPass[0], userPass[1])
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
}
