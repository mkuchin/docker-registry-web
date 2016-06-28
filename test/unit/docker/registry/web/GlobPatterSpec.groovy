package docker.registry.web

import spock.lang.Specification

class GlobPatterSpec extends Specification {
  void "testGlob"() {

    expect:
    GlobMatcher.check(glob, path) == result

    where:
    glob                 | path            | result
    'repo/**'            | 'repo/ubuntu'   | true
    'repo/**'            | 'repox/ubuntu'  | false
    'repo/{ubuntu,java}' | 'repo/java'     | true
    '192.168.*'          | '192.168.1.100' | true
    '192.168.*'          | '192.160.1.1'   | false
    '192.168.???.1'      | '192.168.1.1'   | false
    '192.168.???.1'      | '192.168.100.1' | true
  }
}
