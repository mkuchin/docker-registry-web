package docker.registry.web

import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(FormatTagLib)
class SizeFormatSpec extends Specification {

  void "test size fomrat"() {
    expect:
    applyTemplate('<g:formatSize value="${3000}"/>') == '3 KB'
    applyTemplate('<g:formatSize value="${245}"/>') == '245 B'
    applyTemplate('<g:formatSize value="${1048576}"/>') == '1.0 MB'
    applyTemplate('<g:formatSize value="${1370945784}"/>') == '1.4 GB'

  }
}