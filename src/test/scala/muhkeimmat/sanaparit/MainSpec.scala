package muhkeimmat.sanaparit

import org.scalatest.{FunSpec, Matchers}

class MainSpec extends FunSpec with Matchers {

  describe("Main") {
    it("should find 21 size and 9 word pairs from Alastalon Salissa") {
      val start = System.currentTimeMillis()
      val result = Main.run("src/test/resources/alastalon_salissa.txt")
      val stop = System.currentTimeMillis()
      println(s"${stop - start}ms")

      result.size shouldEqual 21
      result.pairs should have size 9
    }
  }
}
