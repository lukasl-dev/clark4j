package dev.lukasl.clark4j

import dev.lukasl.clark4j.errors.ClarkError
import dev.lukasl.clark4j.flag.Flag
import dev.lukasl.clark4j.json.JsonReader
import dev.lukasl.clark4j.token.Token
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

class Clark(
  private var jsonReader: JsonReader? = null,
  private var executable: String = "clark",

  private var input: String = "",

  private var prefixes: Collection<String> = emptyList(),
  private var prefixIgnoreCase: Boolean = false,

  private var labels: Collection<String> = emptyList(),
  private var labelIgnoreCase: Boolean = false
) {

  fun jsonReader(jsonReader: JsonReader) = apply { this.jsonReader = jsonReader }

  fun executable(executable: String) = apply { this.executable = executable }

  fun input(input: String) = apply { this.input = input }

  fun prefixes(prefixes: Collection<String>) = apply { this.prefixes = prefixes }

  fun prefixes(vararg prefixes: String) = apply { this.prefixes(prefixes.toList()) }

  fun prefixIgnoreCase(prefixIgnoreCase: Boolean) = apply { this.prefixIgnoreCase = prefixIgnoreCase }

  fun labels(labels: Collection<String>) = apply { this.labels = labels }

  fun labels(vararg labels: String) = apply { this.labels(labels.toList()) }

  fun labelIgnoreCase(labelIgnoreCase: Boolean) = apply { this.labelIgnoreCase = labelIgnoreCase }

  fun toFlags(): Map<Flag, Any?> = mutableMapOf<Flag, Any?>().also { flags ->
    flags[Flag.INPUT] = this.input

    this.prefixes.forEach { flags[Flag.PREFIX] = it }
    flags[Flag.PREFIX_IGNORE_CASE] = this.prefixIgnoreCase

    this.labels.forEach { flags[Flag.LABEL] = it }
    flags[Flag.LABEL_IGNORE_CASE] = this.labelIgnoreCase

    flags[Flag.ADVANCED] = true
  }

  private fun toArguments(): Collection<String> = this.toFlags().entries.fold(LinkedList<String>()) { acc, entry ->
    acc.also {
      it.add(entry.key.flag)
      it.add(entry.value.toString())
    }
  }.toList()

  @Throws(IOException::class)
  fun run(): Collection<Token> {
    println("args: '${toArguments().joinToString(separator = ", ")}'")
    val process = Runtime.getRuntime().exec((listOf(this.executable) + this.toArguments()).toTypedArray())
    val reader = InputStreamReader(process.inputStream)

    val tokens = this.jsonReader?.read(reader) ?: throw ClarkError("JsonReader cannot be null.")

    reader.close()
    process.destroy()

    return tokens
  }

  companion object {
    fun create(): Clark = Clark()
  }
}
