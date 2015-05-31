package muhkeimmat.sanaparit

case class Word(value: String, unique: Set[Char]) {
  def size: Int = unique.size
}

case class WordPair(w1: Word, w2: Word, unique: Set[Char]) {
  def size: Int = unique.size
}

case class WordPairGroup(size: Int, pairs: List[WordPair])

object Word {
  def apply(value: String): Word = {
    new Word(value, value.toSet)
  }
}

object WordPair {
  def apply(w1: Word, w2: Word): WordPair = {
    val words = normalize(w1, w2)
    new WordPair(words._1, words._2, words._1.unique ++ words._2.unique)
  }

  private def normalize(w1: Word, w2: Word): (Word, Word) = {
    if (w1.value.compareTo(w2.value) < 0) (w1, w2)
    else (w2, w1)
  }
}
