import scala.util.chaining._

class Set2 extends munit.FunSuite {
  test("Challenge 1") {
    val input = "YELLOW SUBMARINE".pipe(asciiToBytes)
    val expected = "YELLOW SUBMARINE\u0004\u0004\u0004\u0004".pipe(asciiToBytes)
    val actual = pkcs7(input, 20)
    assertEquals(actual, expected)
  }
}
