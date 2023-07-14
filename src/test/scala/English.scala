import scala.util.chaining._

class English extends munit.FunSuite {
  test("Valid english gets high score") {
    val input = "When in the Course of human events it becomes necessary for one people to dissolve the political bands which have connected them with another, and to assume among the Powers of the earth, the separate and equal station to which the Laws of Nature and of Nature's God entitle them, a decent respect to the opinions of mankind requires that they should declare the causes which impel them to the separation.".pipe(asciiToBytes)
    val actual = scoreEnglish(input)
    assert(actual >= 0.05)
  }
  test("Invalid bytes gets low score") {
    val input = scala.util.Random.nextBytes(128).toList
    val actual = scoreEnglish(input)
    assert(actual < 0.0)
  }
}
