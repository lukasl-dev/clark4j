package dev.lukasl.clark4j.json

import dev.lukasl.clark4j.token.Token
import java.io.IOException
import java.io.Reader

interface JsonReader {
  @Throws(IOException::class)
  fun read(reader: Reader): Collection<Token>
}
