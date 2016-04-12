package docker.registry.web

import grails.plugins.rest.client.RequestCustomizer
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpStatusCodeException

class CustomRestBuilder extends RestBuilder {

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
