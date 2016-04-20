package docker.registry.web

import docker.registry.Role
import docker.registry.User
import docker.registry.UserRole
import grails.transaction.Transactional

@Transactional
class UserController {

  def index() {
    def users = User.list()
    [list: users]
  }

  def show() {
    def user = User.get(params.id)
    def roles = Role.list() - user.authorities
    [user: user, roles: roles]
  }

  def edit() {
    def user = User.get(params.id)
    [user: user]
  }

  def addRole() {
    def user = User.get(params.userId)
    def role = Role.get(params.roleId)
    new UserRole(user: user, role: role).save(failOnError: true)
    redirect(action: 'show', id: params.userId)
  }

  def deleteRole() {
    def user = User.get(params.userId)
    def role = Role.get(params.id)
    def userRole = UserRole.findByUserAndRole(user, role)
    userRole.delete()
    redirect(action: 'show', id: params.userId)
  }

  def update() {
    def user = User.get(params.id)
    user.properties = params
    redirect action: 'show', id: params.id
  }

  def delete() {
    def user = User.get(params.id)
    UserRole.findAllByUser(user)*.delete()
    user.delete()
    redirect action: 'index'
  }

  def add() {
  }

  def create() {
    def user = new User(params)
    user.save()
    redirect action: 'show', id: user.id
  }
}
