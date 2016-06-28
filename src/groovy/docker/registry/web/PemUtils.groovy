package docker.registry.web

import org.apache.commons.codec.binary.Base32
import org.bouncycastle.asn1.DERNull
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo
import org.bouncycastle.asn1.pkcs.RSAPrivateKey
import org.bouncycastle.asn1.pkcs.RSAPublicKey
import org.bouncycastle.asn1.x509.AlgorithmIdentifier
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
import org.bouncycastle.openssl.PEMKeyPair
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter

import java.security.KeyPair
import java.security.MessageDigest
import java.security.PublicKey

class PemUtils {

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
    PEMKeyPair pemKeyPair
    if (object instanceof PrivateKeyInfo) {
      pemKeyPair = getPemKeyPair(object)
    } else if (object instanceof PEMKeyPair) {
      pemKeyPair = object
    } else {
      throw new RuntimeException("Key file type not supported: ${object.class}")
    }
    return converter.getKeyPair(pemKeyPair)
  }

  private static PEMKeyPair getPemKeyPair(PrivateKeyInfo pk) {
    def rsaPk = RSAPrivateKey.getInstance(pk.parsePrivateKey())
    def pubSpec = new RSAPublicKey(rsaPk.getModulus(), rsaPk.getPublicExponent())
    AlgorithmIdentifier algId = new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, DERNull.INSTANCE)
    def pemKeyPair = new PEMKeyPair(new SubjectPublicKeyInfo(algId, pubSpec), new PrivateKeyInfo(algId, rsaPk))
    pemKeyPair
  }

  private static Object getPemObject(File file) {
    PEMParser pemParser = new PEMParser(new FileReader(file));
    Object object = pemParser.readObject();
    object
  }
}
