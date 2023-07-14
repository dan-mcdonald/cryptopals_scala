def pkcs7(data: List[Byte], size: Int): List[Byte] = {
  assume(size > data.length, "Size must be greater than data length")
  val remainder = size - data.length
  assume(remainder < 256, "Number of padding bytes must be less than 256")
  val tail = LazyList.continually(remainder.toByte).take(remainder)
  data ++ tail
}
