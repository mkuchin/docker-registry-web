package docker.registry.web

class ModalTagLib {
  def modal = { attrs, body ->
    out << g.render(template: "/templates/modal", model: [id: attrs.id, title: attrs.title, body: body(), fields: attrs.fields])
  }
}
