package docker.registry.web

import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(FormatTagLib)
class SizeFormatSpec extends Specification {

  void "test size format"() {
    expect:
    tagLib.formatSize(value: value).toString() == result
    where:
    value      | result
    3000       | '3 KB'
    245        | '245 B'
    1048576    | '1.0 MB'
    1370945784 | '1.4 GB'
  }
}