class UrlMappings {

  static mappings = {

    "/"(controller: 'repository', action: 'index')

    "/$action/$id?/$name?/"(controller: "repository")

    "/api/auth"(controller: 'auth', action: 'index')

    "500"(view: '/error')
  }
}
