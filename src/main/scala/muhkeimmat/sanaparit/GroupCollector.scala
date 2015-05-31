package muhkeimmat.sanaparit

import rx.lang.scala.Subscriber

import scala.collection.mutable

class GroupCollector(child: Subscriber[WordPairGroup]) extends Subscriber[WordPair] {

  private val groups: mutable.MultiMap[Int, WordPair] = new mutable.HashMap[Int, mutable.Set[WordPair]] with mutable.MultiMap[Int, WordPair]

  override def onNext(value: WordPair): Unit = {
    groups.addBinding(value.size, value)
    request(1)
  }

  override def onError(error: Throwable): Unit = {
    child.onError(error)
  }

  override def onCompleted(): Unit = {
    groups.foreach(g => child.onNext(WordPairGroup(g._1, g._2.toList)))
    println(s"Total number of word size groups: ${groups.size}")
    child.onCompleted()
  }
}

object GroupCollector {
  def apply(child: Subscriber[WordPairGroup]): GroupCollector = {
    new GroupCollector(child)
  }
}
