package io.github.kinasr.playwright_demo_maven.utils

/**
 * Requires that the specified string is not blank and throws an [IllegalArgumentException]
 * with the given message if it is.
 *
 * @param value The string to check
 * @param fieldName The name of the field being validated (used in error message)
 * @return The original string if it's not blank
 * @throws IllegalArgumentException if the string is blank
 */
fun requireNotBlank(value: String, fieldName: String): String {
    require(value.isNotBlank()) { "$fieldName cannot be blank" }
    return value
}

/**
 * Requires that the specified string is not blank and throws an [IllegalArgumentException]
 * with the given message if it is.
 *
 * @param value The string to check
 * @param lazyMessage A function that provides the exception message if the check fails
 * @return The original string if it's not blank
 * @throws IllegalArgumentException if the string is blank
 */
fun requireNotBlank(value: String, lazyMessage: () -> Any): String {
    require(value.isNotBlank(), lazyMessage)
    return value
}
