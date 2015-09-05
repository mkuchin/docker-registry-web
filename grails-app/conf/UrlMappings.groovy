class UrlMappings {

  static mappings = {

    "/"(controller: 'repository', action: 'index')

    "/$action/$id?/$name?/"(controller: "repository")

    "500"(view: '/error')
  }
}
