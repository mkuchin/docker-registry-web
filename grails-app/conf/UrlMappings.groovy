class UrlMappings {

  static mappings = {
    "/$controller/$action?/$id?(.$format)?" {
      constraints {
        // apply constraints here
      }
    }

    "/$action?/$id?"(controller: "repository")
    "500"(view: '/error')
  }
}
