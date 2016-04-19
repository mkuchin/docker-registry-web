package docker.registry.web

import docker.registry.AccessControl
import docker.registry.Role
import docker.registry.RoleAccess
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
    log.info params
    def role = Role.get(params.roleId)
    log.info role
    def acl = AccessControl.get(params.id)
    log.info acl
    def roleAccess = RoleAccess.findByRoleAndAcl(role, acl)
    log.info roleAccess
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
}
