package dev.lukasl.clark4j.errors

class ClarkError(
  override val message: String = "",
  override val cause: Throwable? = null
) : Error(message, cause)
