package docker.registry.web

import org.apache.commons.io.FileUtils

class FormatTagLib {
  static defaultEncodeAs = [taglib: 'html']

  def formatSize = { attrs, body ->
    out << FileUtils.byteCountToDisplaySize(attrs.value)
  }
}
