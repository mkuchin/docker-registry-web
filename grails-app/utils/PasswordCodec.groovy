import java.security.MessageDigest

class PasswordCodec {
  static encode = { String s ->
    MessageDigest md = MessageDigest.getInstance('SHA-256')
    md.update s.getBytes('UTF-8')
    md.digest().encodeAsBase64()
  }

}
