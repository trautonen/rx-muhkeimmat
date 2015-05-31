package muhkeimmat.sanaparit

object Main extends App {

  def help(): Unit = {
    println("usage: sbt \"run <path-to-wordfile>\"")
  }

  def run(path: String): WordPairGroup = {
    new MemoryFile(path).observe
      .lift[Word](WordCollector.apply)
      .lift[WordPair](PairCollector.apply)
      .lift[WordPairGroup](GroupCollector.apply)
      .reduce((g1, g2) => if (g1.size > g2.size) g1 else g2)
      .toBlocking
      .first
  }

  def output(value: WordPairGroup): Unit = {
    println(s"Score: ${value.size}")
    value.pairs.foreach(p => println(s"${p.w1.value} - ${p.w2.value}"))
  }

  val file = args.headOption

  if (file.isDefined) output(run(file.get))
  else help()

  ExecutionContextImplicits.runner.shutdownNow()
}
