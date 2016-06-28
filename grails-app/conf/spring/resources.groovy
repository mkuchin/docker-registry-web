import org.springframework.beans.factory.config.YamlPropertiesFactoryBean
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.core.io.PathResource

// Place your Spring DSL code here
beans = {

  yamlConfig(YamlPropertiesFactoryBean) {
    setProperty('resources', ['WEB-INF/config.yml', new PathResource(application.config.yaml.path)])
  }

  propertyConfig(PropertySourcesPlaceholderConfigurer) {
    properties = ref('yamlConfig')
  }
}
