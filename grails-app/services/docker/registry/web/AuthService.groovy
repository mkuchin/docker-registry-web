package docker.registry.web

import docker.registry.Role
import docker.registry.RoleAccess
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class AuthService {
  AuthenticationManager authenticationManager


  List login(String username, String password) {
    def authToken = new UsernamePasswordAuthenticationToken(username, password)
    log.info "authenticating user: $username"
    def authResult = authenticationManager.authenticate(authToken)
    log.info authResult
    log.info "Granted roles: ${authResult.authorities}"
    //could be ROLE_NO_ROLES if no roles found
    def roles = authResult.authorities.collect { role ->
      def roleName = role.authority
      Role.findByAuthority(roleName)
    }.findAll { it }.collect { role ->
      RoleAccess.findAllByRole(role).acl
    }.flatten()
    roles
  }
}
