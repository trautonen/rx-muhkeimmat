package muhkeimmat.sanaparit

import rx.lang.scala.Subscriber

class WordCollector(child: Subscriber[Word]) extends Subscriber[Char] {

  private var word: StringBuilder = new StringBuilder(64)
  private var wordCount: Long = 0

  override def onNext(value: Char): Unit = {
    val valid = isValidLetter(value)
    if (valid) {
      word += value.toLower
      request(1)
    } else if (!valid && word.nonEmpty) {
      append()
    } else {
      request(1)
    }
  }

  override def onError(error: Throwable): Unit = {
    child.onError(error)
  }

  override def onCompleted(): Unit = {
    if (word.nonEmpty) {
      append()
    }
    println(s"Total number of words: $wordCount")
    child.onCompleted()
  }

  private def append() = {
    child.onNext(Word(word.mkString))
    wordCount = wordCount + 1
    word.clear()
  }

  private def isValidLetter(value: Char): Boolean = {
    (value >= 'a' && value <= 'z') || value == 'å' || value == 'ä' || value == 'ö' ||
    (value >= 'A' && value <= 'Z') || value == 'Å' || value == 'Ä' || value == 'Ö'
  }
}

object WordCollector {
  def apply(child: Subscriber[Word]): WordCollector = {
    new WordCollector(child)
  }
}
