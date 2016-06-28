package docker.registry.web

import java.text.SimpleDateFormat

class DateConverter {
  private static final format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

  static def convert(String date) {
    // docker uses ISO8601 dates w/ fractional seconds (i.e. yyyy-MM-ddTHH:mm:ss.ssssssssZ),
    // which seem to confuse the Date parser, so truncate the timestamp and always assume UTC tz.
    if (date) {
      def dateSubstring = date?.substring(0, 19)
      return format.parse(dateSubstring)
    } else
      return null
  }
}
