import scala.util.chaining._

// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
class Set1 extends munit.FunSuite {
  test("Challenge 1") {
    val input = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d"
    val expected = "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t"
    val actual = hexToBytes(input).pipe(bytesToBase64)
    assertEquals(actual, expected)
  }
  test("Challenge 2") {
    val pt = "1c0111001f010100061a024b53535009181c".pipe(hexToBytes)
    val key = "686974207468652062756c6c277320657965".pipe(hexToBytes)
    val expected = "746865206b696420646f6e277420706c6179"
    val actual = xor(pt, key).pipe(bytesToHex)
    assertEquals(actual, expected)
  }
  test("xor1byte") {
    val ct = "5034521080".pipe(asciiToBytes)
    val key: List[Byte] = List(0x01)
    val expected = "4125430191".pipe(asciiToBytes)
    val actual = xor(ct, key)
    assertEquals(actual, expected)
  }
  test("Challenge 3") {
    val ct = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736".pipe(hexToBytes)
    val expected = "Cooking MC's like a pound of bacon"
    val (_, pt, _) = breakXor1byte(ct)
    val actual = pt.pipe(bytesToAscii)
    assertEquals(actual, expected)
  }
  test("Challenge 4") {
    val inputs = resourceLines("s1c4.txt").map(hexToBytes)
    val attempted = inputs.map(breakXor1byte)
    val foundBroken = attempted.filter((_, _, score) => isEnglish(score))
    val actual = foundBroken.map((_, pt, _) => bytesToAscii(pt)).toList
    val expected = List("Now that the party is jumping\n")
    assertEquals(actual, expected)
  }
  test("Challenge 5") {
    val input = "Burning 'em, if you ain't quick and nimble\nI go crazy when I hear a cymbal".pipe(asciiToBytes)
    val key = "ICE".pipe(asciiToBytes)
    val ct = xor(input, key)
    val expected = "0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f"
    val actual = ct.pipe(bytesToHex)
    assertEquals(actual, expected)
  }
  test("hamming") {
    val a = "this is a test".pipe(asciiToBytes)
    val b = "wokka wokka!!!".pipe(asciiToBytes)
    val actual = hamming(a, b)
    val expected = 37
    assertEquals(actual, expected)
  }
  test("Challenge 6") {
    val inputLines = resourceLines("s1c6.txt")
    val ct = inputLines.mkString("").pipe(base64ToBytes)
    val (key, pt) = breakXorRepeating(ct)
    val expected = "Terminator X: Bring the noise"
    val actual = key.pipe(bytesToAscii)
    assertEquals(actual, expected)
  }
  test("Challenge 7") {
    val input = resourceLines("s1c7.txt").mkString("").pipe(base64ToBytes)
    val pt = decryptAes128Ecb(input, "YELLOW SUBMARINE".pipe(asciiToBytes))
    assert(isEnglish(scoreEnglish(pt)))
  }
  test("Challenge 8") {
    val inputLines = resourceLines("s1c8.txt").map(base64ToBytes)
    val actual = inputLines.filter(hasCollision128).map(bytesToHex)
    val expected = List("77cf34eb5f7be346bc6b5f5befce346bc6b7d5cf35d1adddd3ceb8f5a7fbd1d734e9fe1f77977677af5cef8e1c776f377b675dd39d9fe9beb8d5d6dff5dd756f4df8f39e366dbe7bd3ceb8f5a7fbd1d734e9fe1f77977677af5cef8e1c776f37f78ef973d75f75b735778eb9f7bf78f5df5cedef366dfe5ad3ceb8f5a7fbd1d734e9fe1f77977677af5cef8e1c776f37f7b6bddde69bf1de9a79c779ebae3cf75e78efcf5ae9bd37d3ceb8f5a7fbd1d734e9fe1f77977677af5cef8e1c776f37778d37d7cd1cf7c73c7fa75bd5fd9addff5ce34e3475e6f469be756f6f7ddf77f6735db7739f37f3a6f4e9f6dad7ce9a")
    assertEquals(actual, expected)
  }
}
