package docker.registry.web


class SizeFormat {
  private static final String suffixes = "KMGTPE"
  private static int unit = 1000

  static String format(BigInteger bytes) {
    if (bytes < unit)
      return bytes + " B"
    int exp = (int) (Math.log(bytes.toDouble()) / Math.log(unit))
    String pre = suffixes.charAt(exp - 1)
    //show decimal digit only from MB
    int decimal = exp > 1 ? 1 : 0
    return String.format("%.${decimal}f %sB", bytes / Math.pow(unit, exp), pre)
  }
}
