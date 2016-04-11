package docker.registry.web

import org.apache.commons.codec.binary.Base32
import org.bouncycastle.openssl.PEMKeyPair
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter

import java.security.KeyPair
import java.security.MessageDigest
import java.security.PublicKey
import java.security.interfaces.RSAPrivateKey

class PemUtils {
  public static RSAPrivateKey generatePrivateKeyFromPEM(File keyFile) {
    def kp = getKeyPair(keyFile)
    return (RSAPrivateKey) kp.private
  }

  public static getKeyDigest(PublicKey publicKey) {
    def digest = MessageDigest.getInstance("SHA-256").digest(publicKey.encoded)
    def payload = new byte[30]
    System.arraycopy(digest, 0, payload, 0, 30)
    def base32Encoder = new Base32()
    ((new String(base32Encoder.encode(payload))).chars as List).collate(4).collect { it.join('') }.join(':')
  }

  public static KeyPair getKeyPair(File file) {
    Object object = getPemObject(file)
    JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC")
    return converter.getKeyPair((PEMKeyPair) object)
  }


  private static Object getPemObject(File file) {
    PEMParser pemParser = new PEMParser(new FileReader(file));
    Object object = pemParser.readObject();
    object
  }
}
