package docker.registry.web

class FormatTagLib {
  static defaultEncodeAs = [taglib: 'html']

  def formatSize = { attrs, body ->    
    if(attrs.value != null){
      out << SizeFormat.format(attrs.value)
    }
  }
}
