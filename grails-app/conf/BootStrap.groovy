import docker.registry.*
import docker.registry.acl.AccessLevel
import docker.registry.web.TrustAnySSL
import grails.plugin.springsecurity.web.access.intercept.InterceptUrlMapFilterInvocationDefinition
import grails.util.Environment

class BootStrap {
  def grailsApplication
  def configService
  def filterInvocationInterceptor

  def init = { servletContext ->

    log.info "Starting registry-web"
    configService.dump()

    def authEnabled = configService.authEnabled
    log.info "auth enabled: ${authEnabled}"

    if (authEnabled) {
      filterInvocationInterceptor.rejectPublicInvocations = true
      def config = grailsApplication.config
      config.grails.plugin.springsecurity.interceptUrlMap = config.auth.InterceptUrlMap
      def filterDef = (InterceptUrlMapFilterInvocationDefinition) filterInvocationInterceptor.securityMetadataSource
      filterDef.reset()
    }

    //initializing auth if no roles or users exists
    if (!(Role.list() || User.list())) {
      def admin = new User(username: 'admin', password: 'admin').save(failOnError: true)
      def uiRole = new Role('UI_USER').save()
      def uiAdminRole = new Role('UI_ADMIN').save()
      def readRole = new Role('read-all').save(failOnError: true)
      def writeRole = new Role('write-all').save(failOnError: true)

      def readAll = new AccessControl(name: '**', ip: '*', level: AccessLevel.PULL).save(failOnError: true)
      def writeAcl = new AccessControl(name: '**', ip: '*', level: AccessLevel.PUSH).save(failOnError: true)

      UserRole.create(admin, uiAdminRole, true)
      RoleAccess.create(readRole, readAll)
      RoleAccess.create(writeRole, writeAcl)

      //log.info authService.login("test", "testPassword")
    }
    if (Environment.current == Environment.DEVELOPMENT) {
      def uiRole = Role.findByAuthority('UI_USER')
      def testUser = new User(username: 'test', password: 'test').save(failOnError: true)
      UserRole.create(testUser, uiRole, true)
      (1..100).each { i ->
        new Event(username: 'test', repo: 'some', ip: "$i.$i.$i.$i", action: 'pull', tag: 'latest', time: new Date()).save()
      }
    }

    if (configService.trustAnySsl) {
      log.info "Trusting any SSL certificate"
      TrustAnySSL.init()
    }
  }
  def destroy = {
  }
}
