package dev.lukasl.clark4j.token

data class Token(
  val code: Int,
  val type: TokenType,
  val value: String
)
