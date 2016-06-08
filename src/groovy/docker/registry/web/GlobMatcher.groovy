package docker.registry.web

import java.nio.file.FileSystems
import java.nio.file.Paths

class GlobMatcher {

  static boolean check(String glob, String path) {
    def matcher = FileSystems.getDefault().getPathMatcher("glob:" + glob)
    matcher.matches(Paths.get(path))
  }
}
