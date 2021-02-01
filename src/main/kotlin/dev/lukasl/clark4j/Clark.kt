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

  fun toFlags(): Collection<Pair<Flag, Any?>> = mutableListOf<Pair<Flag, Any?>>().also { flags ->
    flags.add(Pair(Flag.INPUT, "\"$input\""))

    this.prefixes.forEach { flags.add(Pair(Flag.PREFIX, "\"$it\"")) }
    flags.add(Pair(Flag.PREFIX_IGNORE_CASE, this.prefixIgnoreCase))

    this.labels.forEach { flags.add(Pair(Flag.LABEL, "\"$it\"")) }
    flags.add(Pair(Flag.LABEL_IGNORE_CASE, this.labelIgnoreCase))

    flags.add(Pair(Flag.ADVANCED, true))
  }.toList()

  private fun toArguments(): Collection<String> = this.toFlags().fold(LinkedList<String>()) { acc, entry ->
    acc.also {
      it.add(entry.first.flag)
      it.add(entry.second.toString())
    }
  }.toList()

  @Throws(IOException::class)
  fun run(): Collection<Token> {
    val args = (listOf(this.executable) + this.toArguments()).toTypedArray()
    println(args.joinToString(separator = " "))
    val process = Runtime.getRuntime().exec(args)
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
