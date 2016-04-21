import org.springframework.beans.factory.config.YamlPropertiesFactoryBean
import org.springframework.core.io.PathResource

// Place your Spring DSL code here
beans = {
  yamlConfig(YamlPropertiesFactoryBean) {
    setProperty('resources', new PathResource('grails-app/conf/config.yml'))
  }
}
