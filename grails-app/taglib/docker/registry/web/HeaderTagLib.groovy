package docker.registry.web

class HeaderTagLib {
  static defaultEncodeAs = [taglib: 'none']
  def header = { attrs, body ->
    out << g.render(template: "/templates/header", model: [title: attrs.title, body: body()])
  }
}
