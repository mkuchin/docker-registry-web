package docker.registry.web

import grails.plugins.rest.client.RequestCustomizer
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpStatusCodeException

class CustomRestBuilder extends RestBuilder {

  CustomRestBuilder() {
    // workaround for
    // https://github.com/grails-plugins/grails-rest-client-builder/issues/40
    this.restTemplate.messageConverters.removeAll {
      it.class.name == 'org.springframework.http.converter.json.GsonHttpMessageConverter'
    }
  }

  RestResponse request(HttpMethod method, String url, RequestCustomizer requestCustomizer) {
    try {
      ResponseEntity responseEntity = invokeRestTemplate(url, method, requestCustomizer)
      handleResponse(responseEntity)
    }
    catch (HttpStatusCodeException e) {
      return new RestResponse(new ResponseEntity(e.getResponseBodyAsString(), e.responseHeaders, e.statusCode))
    }
  }
}
