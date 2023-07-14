def pkcs7(data: List[Byte], size: Int): List[Byte] = {
  val remainder = size - data.length
  val tail = LazyList.continually(remainder.toByte).take(remainder)
  data ++ tail
}
