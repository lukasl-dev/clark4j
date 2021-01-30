package dev.lukasl.clark4j.json

import dev.lukasl.clark4j.token.Token
import java.io.Reader

class FunctionalJsonReader(
  private val reader: (Reader) -> Collection<Token>
) : JsonReader {
  override fun read(reader: Reader): Collection<Token> = this.reader.invoke(reader)
}
