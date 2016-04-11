package docker.registry.web

import grails.converters.JSON
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.beans.factory.annotation.Value

import javax.annotation.PostConstruct
import java.security.KeyPair
import java.security.PrivateKey
import java.security.Security
import java.security.Signature

class TokenService {

  @Value('${registry.keyFile}')
  String keyFilename

  @Value('${registry.name}')
  String registryName
  private KeyPair keyPair
  private String keyDigest

  @PostConstruct
  def init() {
    Security.addProvider(new BouncyCastleProvider())
    def file = new File(keyFilename)
    if (file.exists()) {
      this.keyPair = PemUtils.getKeyPair(file)
      this.keyDigest = PemUtils.getKeyDigest(keyPair.public)
    } else {
      log.warn "No AUTH_KEYFILE configured"
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

  String generate(String subject, List access) {
    int time = System.currentTimeMillis() / 1000
    def headerMap = [alg: "RS256", typ: "JWT", kid: keyDigest]
    def payloadMap = [
        iss   : 'test-issuer',
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
    log.info "Header: $headerMap"
    log.info "Payload: $payloadMap"

    def signature = sign(header, payload, keyPair.private)
    "${header}.${payload}.${signature}"
  }
}
