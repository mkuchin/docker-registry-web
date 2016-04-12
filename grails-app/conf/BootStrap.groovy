import docker.registry.web.TrustAnySSL
import org.springframework.beans.factory.annotation.Value

class BootStrap {
  def restService

  @Value('${ssl.trustAny}')
  boolean trustAny

  def init = { servletContext ->
    if (System.env.TRUST_ANY_SSL == 'true') {
      log.info "Trusting any SSL certificate"
      TrustAnySSL.init()
    }
    restService.init()
  }
  def destroy = {
  }
}
