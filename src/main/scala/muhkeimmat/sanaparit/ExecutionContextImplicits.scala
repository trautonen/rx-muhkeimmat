package muhkeimmat.sanaparit

import java.util.concurrent.Executors

import scala.concurrent.ExecutionContext

object ExecutionContextImplicits {

  implicit lazy val runner = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(4))

}
