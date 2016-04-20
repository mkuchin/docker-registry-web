package docker.registry.web

import docker.registry.AccessControl
import docker.registry.Role
import docker.registry.RoleAccess
import docker.registry.UserRole
import grails.transaction.Transactional

@Transactional
class RoleController {
  def index() {
    def roles = Role.list()
    [roles: roles]
  }

  def show() {
    def role = Role.get(params.id)
    def acls = RoleAccess.findAllByRole(role).acl
    [role: role, acls: acls]
  }

  def deleteAcl() {
    def role = Role.get(params.roleId)
    def acl = AccessControl.get(params.id)
    log.info "Deleting acl=${acl} from role.id=${role}"
    def roleAccess = RoleAccess.findByRoleAndAcl(role, acl)
    roleAccess.delete()
    acl.delete()
    redirect action: 'show', id: params.roleId
  }

  def addAcl() {
    def role = Role.get(params.id)
    def acl = new AccessControl(params)
    acl.save()
    def roleAccess = new RoleAccess(role: role, acl: acl)
    roleAccess.save()
    redirect action: 'show', id: params.id
  }

  def add() {}

  def create() {
    def role = new Role(authority: params.role)
    role.save(failOnError: true)
    redirect action: 'show', id: role.id
  }

  def delete() {
    def role = Role.get(params.id)
    log.info "Deleting role=${role}"
    UserRole.findByRole(role)*.delete()
    def roleAccess = RoleAccess.findAllByRole(role)
    def acls = roleAccess.acl
    log.info "Deleting acls: ${acls}"
    roleAccess*.delete()
    acls*.delete()
    role.delete()
    redirect action: 'index'
  }
}
