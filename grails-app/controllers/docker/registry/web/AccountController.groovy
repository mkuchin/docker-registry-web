package docker.registry.web

import docker.registry.Event
import docker.registry.User
import grails.transaction.Transactional

class AccountController {

  def passwordEncoder
  def springSecurityService

  def index() {
    def user = springSecurityService.currentUser
    def events = Event.findAllByUsername(user.username, [max: 20, order: 'desc', sort: 'id'])
    [user: user, events: events]
  }


  @Transactional
  def updatePassword() {
    String oldPassword = params.oldPassword
    String newPassword = params.newPassword
    String newPasswordRepeat = params.newPasswordRepeat
    User user = springSecurityService.currentUser
    //validates raw password against hashed
    if (!passwordEncoder.isPasswordValid(user.password, oldPassword, null)) {
      flash.message = 'password.incorrect'
      log.warn flash.message
      redirect(action: 'changePassword')
    } else {
      if (newPassword.length() == 0) {
        flash.message = 'password.empty'
        log.warn flash.message
        redirect(action: 'changePassword')
      } else if (newPassword != newPasswordRepeat) {
        flash.message = 'password.notmatch'
        log.warn flash.message
        redirect(action: 'changePassword')
      } else {
        log.warn "Updating password for user=${user}"
        user.password = newPassword
        flash.message = 'password.updated'
        redirect(action: 'index')
      }
    }
  }

  def changePassword() {
    def user = springSecurityService.currentUser
    [user: user]
  }
}
