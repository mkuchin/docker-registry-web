package docker.registry.web

import docker.registry.Event

class EventController {

  def index() {
    def total = Event.count()
    def list = Event.list max: 20, offset: params.offset, sort: 'id', order: 'desc'
    [list: list, total: total]
  }
}
