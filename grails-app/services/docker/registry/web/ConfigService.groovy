package docker.registry.web

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.PropertySourcesPropertyResolver

class ConfigService {

  def propertyConfig
  def yamlConfig

  @Value('${registry.auth.enabled}')
  boolean authEnabled

  @Value('${registry.trust_any_ssl}')
  boolean trustAnySsl

  def dump() {
    def configResolver = new PropertySourcesPropertyResolver(propertyConfig.appliedPropertySources)
    log.info propertyConfig.appliedPropertySources*.name
    def configProperties = Collections.list(yamlConfig.keys()).collectEntries {
      key -> [key, configResolver.getProperty(key)]
    }

    log.info "resolved config:"
    configProperties.each { key, value ->
      log.info "${key}: ${value}"
    }
  }

}
