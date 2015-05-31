package muhkeimmat.sanaparit

import java.util.concurrent.ConcurrentSkipListSet
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.IntUnaryOperator

import rx.lang.scala.Subscriber

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.concurrent.Future

class PairCollector(child: Subscriber[WordPair]) extends Subscriber[Word] {
  import ExecutionContextImplicits.runner

  private val wordOrdering: Ordering[Word] = Ordering.by(w => (w.size, w.value))
  private val words: ConcurrentSkipListSet[Word] = new ConcurrentSkipListSet(wordOrdering.reverse)
  private val biggestPair: AtomicInteger = new AtomicInteger(0)
  private val futures: ListBuffer[Future[Unit]] = ListBuffer()

  override def onNext(value: Word): Unit = {
    if (!words.contains(value)) {
      futures += spawn(value, words.asScala)
      words.add(value)
    } else {
      request(1)
    }
  }

  override def onError(error: Throwable): Unit = {
    child.onError(error)
  }

  override def onCompleted(): Unit = {
    Future.sequence(futures).foreach(l => {
      println(s"Total number of unique words: ${words.size}")
      child.onCompleted()
    })
  }

  private def spawn(value: Word, words: mutable.Set[Word]): Future[Unit] = Future {
    words.takeWhile(w => value.size + w.size >= biggestPair.get()).foreach(w => {
      val pair = WordPair(value, w)
      val current = biggestPair.getAndUpdate(new IntUnaryOperator {
        override def applyAsInt(operand: Int): Int = {
          if (pair.size > operand) pair.size else operand
        }
      })
      if (pair.size >= current) {
        child.onNext(pair)
      }
    })
  }

}

object PairCollector {
  def apply(child: Subscriber[WordPair]): PairCollector = {
    new PairCollector(child)
  }
}
