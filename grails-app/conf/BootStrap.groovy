import docker.registry.web.AuthToken
import docker.registry.web.Repository
import docker.registry.web.RepositoryToken
import docker.registry.web.TrustAnySSL
import org.springframework.beans.factory.annotation.Value

class BootStrap {
  def restService

  @Value('${ssl.trustAny}')
  boolean trustAny

  def init = { servletContext ->

    //initializing auth
    def repo = new Repository(name: 'hello-world').save()
    def tokenRead = new AuthToken(name: 'read', password: 'read'.encodeAsPassword(), read: true, write: false).save()
    def tokenWrite = new AuthToken(name: 'write', password: 'write'.encodeAsPassword(), read: true, write: true).save()
    log.info "token read id: ${tokenRead.id}"
    log.info "token write id: ${tokenWrite.id}"
    new RepositoryToken(repository: repo, token: tokenRead).save()
    new RepositoryToken(repository: repo, token: tokenWrite).save()


    if (System.env.TRUST_ANY_SSL == 'true') {
      log.info "Trusting any SSL certificate"
      TrustAnySSL.init()
    }
    restService.init()
  }
  def destroy = {
  }
}
