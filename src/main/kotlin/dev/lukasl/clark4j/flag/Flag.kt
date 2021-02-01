package dev.lukasl.clark4j.flag

enum class Flag(val flag: String) {
  INPUT(
    flag = "--input"
  ),
  PREFIX(
    flag = "--prefix"
  ),
  PREFIX_IGNORE_CASE(
    flag = "--prefix-ignore-case"
  ),
  LABEL(
    flag = "--label"
  ),
  LABEL_IGNORE_CASE(
    flag = "--label-ignore-case"
  ),
  ADVANCED(
    flag = "--advanced"
  );
}
