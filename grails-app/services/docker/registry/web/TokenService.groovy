package docker.registry.web

import grails.converters.JSON
import grails.plugin.cache.Cacheable
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.beans.factory.annotation.Value

import javax.annotation.PostConstruct
import java.security.KeyPair
import java.security.PrivateKey
import java.security.Security
import java.security.Signature
import java.text.SimpleDateFormat

class TokenService {

  @Value('${registry.auth.key}')
  String keyFilename

  @Value('${registry.auth.enabled}')
  boolean authEnabled

  @Value('${registry.name}')
  String registryName

  @Value('${registry.auth.issuer}')
  String issuer

  private KeyPair keyPair
  private String keyDigest

  @PostConstruct
  def init() {
    Security.addProvider(new BouncyCastleProvider())
    def file = new File(keyFilename)
    if (file.exists()) {
      this.keyPair = PemUtils.getKeyPair(file)
      this.keyDigest = PemUtils.getKeyDigest(keyPair.public)
      log.info "Key file loaded, digest: ${keyDigest}"
    } else {
      if (authEnabled)
        log.warn "Key file not found: ${keyFilename}"
      else
        log.warn "Authorization disabled"
    }
  }

  def isConfigured() {
    keyPair != null
  }

  String sign(String header, String payload, PrivateKey key) {
    def signature = Signature.getInstance("SHA256withRSA", "BC")
    signature.initSign(key)
    byte[] message = (header + '.' + payload).getBytes()
    signature.update(message)
    byte[] sigBytes = signature.sign()
    encodeBase64(sigBytes)
  }

  private String encodeBase64(Object value) {
    encodeBase64(value.toString().bytes)
  }

  private String encodeBase64(byte[] bytes) {
    def encoded = bytes.encodeAsBase64()
    encoded.tr('+/', '-_').replaceAll('=+$', '')
  }

  @Cacheable("tokens")
  Map generate(String subject, List access) {
    if (!authEnabled)
      return null
    int time = System.currentTimeMillis() / 1000
    def headerMap = [alg: "RS256", typ: "JWT", kid: keyDigest]
    def payloadMap = [
        iss   : issuer,
        aud   : registryName, //mirroring auth.token.service from docker registry config
        sub   : subject,
        nbf   : time - 60,
        exp   : time + 3600 * 2,
        iat   : time,
        jti   : UUID.randomUUID().toString(),
        access: access
    ]

    String header = encodeBase64(headerMap as JSON)
    String payload = encodeBase64(payloadMap as JSON)
    //skip logging for internal requests
    if (subject) {
      log.debug "Header: $headerMap"
      log.debug "Payload: $payloadMap"
    }
    def signature = sign(header, payload, keyPair.private)

    def dateFormat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss'Z'")
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"))
    def now = dateFormat.format(new Date())

    [token: "${header}.${payload}.${signature}", expires_in: 3600, issued_at: now]
  }
}
