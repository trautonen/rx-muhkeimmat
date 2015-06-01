package muhkeimmat.sanaparit

import rx.lang.scala.Subscriber

class WordCollector(child: Subscriber[Word]) extends Subscriber[Char] {

  private var word: StringBuilder = new StringBuilder(32)
  private var wordCount: Long = 0

  override def onNext(value: Char): Unit = {
    if (isSplitChar(value) && word.nonEmpty) {
      append()
    } else {
      val lcase = value.toLower
      value.isLetter
      if (isValidLetter(lcase)) word += lcase
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
    (value >= 'a' && value <= 'z') || value == 'å' || value == 'ä' || value == 'ö'
  }

  private def isSplitChar(value: Char): Boolean = {
    value == '.' || value == ',' || value == '/' || value == '\\' || value.isWhitespace
  }
}

object WordCollector {
  def apply(child: Subscriber[Word]): WordCollector = {
    new WordCollector(child)
  }
}
