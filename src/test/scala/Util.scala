def resourceLines(path: String): Iterable[String] = {
  scala.io.Source.fromResource(path).getLines().iterator.to(Iterable)
}
