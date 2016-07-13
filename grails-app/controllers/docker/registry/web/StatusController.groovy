package docker.registry.web

class StatusController {
  def grailsApplication
  def tokenService
  def configService

  def index() {
    def meta = grailsApplication.metadata
    log.info "Starting registry-web ver. ${meta['app.version']}-${meta['app.commit']}"
    def version = meta['app.version']
    def fullHash = meta['app.commit']
    def hash = fullHash ? fullHash[0..6] : 'dev'
    [version: "${version}-${hash}", keyDigest: tokenService.keyDigest, configPath: grailsApplication.config.yaml.path,
     config : configService.configMap()]
  }
}
