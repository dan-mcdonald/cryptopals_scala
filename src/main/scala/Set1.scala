def hexToBytes(hex: String): List[Byte] = {
  val bytes = hex.grouped(2).map(Integer.parseInt(_, 16).toByte)
  bytes.toList
}

def bytesToHex(bytes: List[Byte]): String = {
  val hex = bytes.map("%02x".format(_)).mkString
  hex
}

def bytesToBase64(bytes: List[Byte]): String = {
  val base64 = java.util.Base64.getEncoder.encodeToString(bytes.toArray)
  base64
}

def base64ToBytes(encoded: String): List[Byte] = {
  val bytes = java.util.Base64.getDecoder.decode(encoded)
  bytes.toList
}

def bytesToAscii(bytes: List[Byte]): String = {
  val ascii = bytes.map(_.toChar).mkString
  ascii
}

def asciiToBytes(s: String): List[Byte] = {
  val bytes = s.getBytes.iterator
  bytes.toList
}

def xor(pt: List[Byte], key: List[Byte]): List[Byte] = {
  val xor = pt.zip(Iterator.continually(key).flatten).map { case (a, b) => (a ^ b).toByte }
  xor.toList
}

def breakXor1byte(ct: List[Byte]): (Byte, List[Byte], Double) = {
  val keyspace = (0 to 255).map(_.toByte)
  val allPlaintexts = keyspace.map(key => (key, xor(ct, List(key))))
  val allScores = allPlaintexts.map((key, pt) => (key, pt, scoreEnglish(pt)) )
  val best = allScores.maxBy((_, _, score) => score)
  best
}

def bitCount(n: Byte): Byte = {
  Integer.bitCount(n & 0xff).toByte
}

def hamming(a: List[Byte], b: List[Byte]): Int = {
  a.zip(b)
    .map((a, b) => (a^b) & 0xff)
    .map(Integer.bitCount)
    .sum
}

def normalizedHamming(ct: List[Byte], keysize: Int): Double = {
  val blocks = ct.grouped(keysize).take(4).toList
  val pairs = blocks.combinations(2)
  val distances = pairs.map { case List(a, b) => hamming(a, b) }
  distances.sum / keysize.toDouble
}

def findXorKeysize(ct: List[Byte]): Int = {
  (2 to 40).minBy(keysize => normalizedHamming(ct, keysize))
}

def breakXorRepeating(ct: List[Byte]): (List[Byte], List[Byte]) = {
  val keysize = findXorKeysize(ct)
  val blocks = ct.grouped(keysize).takeWhile(block => block.length == keysize).toList
  val transposed = blocks.transpose
  val key = transposed.map(breakXor1byte(_)._1)
  val pt = xor(ct, key)
  (key, pt)
}

def decryptAes128Ecb(ct: List[Byte], key: List[Byte]): List[Byte] = {
  val cipher = javax.crypto.Cipher.getInstance("AES/ECB/NoPadding")
  val keySpec = new javax.crypto.spec.SecretKeySpec(key.toArray, "AES")
  cipher.init(javax.crypto.Cipher.DECRYPT_MODE, keySpec)
  val pt = cipher.doFinal(ct.toArray)
  pt.toList
}

def hasCollision128(ct: List[Byte]): Boolean = {
  val blocks = ct.grouped(16).toList
  val uniqueBlocks = blocks.toSet
  blocks.length != uniqueBlocks.size
}
