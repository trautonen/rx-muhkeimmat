package muhkeimmat.sanaparit

import java.io.RandomAccessFile
import java.nio.CharBuffer
import java.nio.channels.FileChannel.MapMode
import java.nio.charset.StandardCharsets

import rx.lang.scala.{Subscription, Observable}

class MemoryFile(path: String) {

  def buffer: CharBuffer = {
    val channel = new RandomAccessFile(path, "r").getChannel
    val buffer = channel.map(MapMode.READ_ONLY, 0, channel.size())
    StandardCharsets.UTF_8.decode(buffer)
  }

  def observe: Observable[Char] = {
    Observable.create(observer => {
      val chars = buffer

      while (chars.hasRemaining) {
        observer.onNext(chars.get())
      }
      observer.onCompleted()

      Subscription()
    })
  }

}
