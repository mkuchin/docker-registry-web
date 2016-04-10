package docker.registry.web

import java.text.SimpleDateFormat

class AuthController {
  static String defaultToken = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiIsImtpZCI6IlBZWU86VEVXVTpWN0pIOjI2SlY6QVFUWjpMSkMzOlNYVko6WEdIQTozNEYyOjJMQVE6WlJNSzpaN1E2In0' +
      '.eyJpc3MiOiJhdXRoLmRvY2tlci5jb20iLCJzdWIiOiJqbGhhd24iLCJhdWQiOiJyZWdpc3RyeS5kb2NrZXIuY29tIiwiZXhwIjoxNDE1Mzg3MzE1LCJuYmYiOjE0MTUzODcwMTUsImlhdCI6MTQxNTM4NzAxNSwianRpIjoidFlKQ08xYzZjbnl5N2tBbjBjN3JLUGdiVjFIMWJGd3MiLCJhY2Nlc3MiOlt7InR5cGUiOiJyZXBvc2l0b3J5IiwibmFtZSI6InNhbWFsYmEvbXktYXBwIiwiYWN0aW9ucyI6WyJwdXNoIl19XX0' +
      '.QhflHPfbd6eVF4lM9bwYpFZIV0PfikbyXuLx959ykRTBpe3CYnzs6YBK8FToVb5R47920PVLrh8zuLzdCr9t3w'
  def index() {
    log.info("Auth params: $params")
    log.info("Auth headers")
    request.headerNames.each { name ->
      log.info("  ${name}: ${request.getHeader(name)}")
    }

    def dateFormat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss'Z'")
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"))
    def now = dateFormat.format(new Date())

    def token = System.env.TOKEN ?: defaultToken

    def json = [token: token, expires_in: 3600, issued_at: now]
    log.info "Auth response: $json"
    render(contentType: 'application/json') {
      json
      //{"token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiIsImtpZCI6IlBZWU86VEVXVTpWN0pIOjI2SlY6QVFUWjpMSkMzOlNYVko6WEdIQTozNEYyOjJMQVE6WlJNSzpaN1E2In0
      // .eyJpc3MiOiJhdXRoLmRvY2tlci5jb20iLCJzdWIiOiJqbGhhd24iLCJhdWQiOiJyZWdpc3RyeS5kb2NrZXIuY29tIiwiZXhwIjoxNDE1Mzg3MzE1LCJuYmYiOjE0MTUzODcwMTUsImlhdCI6MTQxNTM4NzAxNSwianRpIjoidFlKQ08xYzZjbnl5N2tBbjBjN3JLUGdiVjFIMWJGd3MiLCJhY2Nlc3MiOlt7InR5cGUiOiJyZXBvc2l0b3J5IiwibmFtZSI6InNhbWFsYmEvbXktYXBwIiwiYWN0aW9ucyI6WyJwdXNoIl19XX0
      // .QhflHPfbd6eVF4lM9bwYpFZIV0PfikbyXuLx959ykRTBpe3CYnzs6YBK8FToVb5R47920PVLrh8zuLzdCr9t3w",
      // "expires_in": "3600","issued_at": "2009-11-10T23:00:00Z"}

      //header:
      //{"typ":"JWT","alg":"ES256","kid":"PYYO:TEWU:V7JH:26JV:AQTZ:LJC3:SXVJ:XGHA:34F2:2LAQ:ZRMK:Z7Q6"}
      //payload:
      //{"iss":"auth.docker.com","sub":"jlhawn","aud":"registry.docker.com","exp":1415387315,"nbf":1415387015,
      // "iat":1415387015,"jti":"tYJCO1c6cnyy7kAn0c7rKPgbV1H1bFws","access":[{"type":"repository","name":"samalba/my-app","actions":["push"]}]}
      //
    }

    /*
    Auth params: [scope:repository:hello-world:push,pull, service:hub.com, account:hub, action:index, controller:auth]
    Auth headers
     host: hub
     x-real-ip: 1.1.1.1
     accept-encoding: gzip
     x-forwarded-proto: https
     connection: close
     user-agent: docker/1.10.3 go/go1.5.3 git-commit/20f81dd kernel/4.1.19-boot2docker os/linux arch/amd64
     authorization: Basic xxxxxxxxx

     */
  }
}
