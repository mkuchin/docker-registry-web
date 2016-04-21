import docker.registry.*
import docker.registry.acl.AccessLevel
import docker.registry.web.TrustAnySSL
import org.springframework.beans.factory.annotation.Value

class BootStrap {
  @Value('${ssl.trustAny}')
  boolean trustAny

  def authService
  def grailsApplication

  def yamlConfig
  def init = { servletContext ->

    def yamlConfigProperties = Collections.list(yamlConfig.propertyNames()).collectEntries {
      key -> [key, yamlConfig.get(key)]
    }
    log.info "Yaml config: $yamlConfigProperties"

    //initializing auth if no roles or users exists
    if (!(Role.list() || User.list())) {
      def user = new User(username: 'test', password: 'testPassword').save(failOnError: true)
      def role = new Role('read-all').save(failOnError: true)
      def write = new Role('write-all').save(failOnError: true)

      def acl = new AccessControl(name: 'hello', ip: '', level: AccessLevel.PULL).save(failOnError: true)
      def writeAcl = new AccessControl(name: 'hello', ip: '', level: AccessLevel.PUSH).save(failOnError: true)
      UserRole.create(user, role, true)
      RoleAccess.create(role, acl)
      RoleAccess.create(write, writeAcl)

      log.info authService.login("test", "testPassword")
    }

    if (System.env.TRUST_ANY_SSL == 'true') {
      log.info "Trusting any SSL certificate"
      TrustAnySSL.init()
    }

    log.info grailsApplication.config.registry
  }
  def destroy = {
  }
}
