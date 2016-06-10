package docker.registry.web

import docker.registry.User
import grails.transaction.Transactional

class AccountController {

  def passwordEncoder
  def springSecurityService

  def index() {
    def user = springSecurityService.currentUser
    [user: user]
  }


  @Transactional
  def updatePassword() {
    String oldPassword = params.oldPassword
    String newPassword = params.newPassword
    String newPasswordRepeat = params.newPasswordRepeat
    User user = springSecurityService.currentUser
    //validates raw password against hashed
    if (!passwordEncoder.isPasswordValid(user.password, oldPassword, null)) {
      flash.message = "Current password is incorrect"
      log.warn flash.message
      redirect(action: 'changePassword')
    } else {
      if (newPassword.length() == 0) {
        flash.message = "New password is empty"
        log.warn flash.message
        redirect(action: 'changePassword')
      } else if (newPassword != newPasswordRepeat) {
        flash.message = "Password does not match the confirm password"
        log.warn flash.message
        redirect(action: 'changePassword')
      } else {
        log.warn "Updating password for user=${user}"
        user.password = newPassword
        flash.message = "Password updated"
        redirect(action: 'index')
      }
    }
  }

  def changePassword() {
    def user = springSecurityService.currentUser
    [user: user]
  }
}
