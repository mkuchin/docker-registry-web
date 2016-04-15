class UrlMappings {

  static mappings = {

    "/"(controller: 'repository', action: 'index')

    "/repo/$action/$id?/$name?/"(controller: "repository")

    "/$controller/$action/$id?"()

    "/api/auth"(controller: 'auth', action: 'index')

    "500"(view: '/error')
  }
}
