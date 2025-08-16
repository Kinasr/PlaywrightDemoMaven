package io.github.kinasr.playwright_demo_maven.utils

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

inline fun String?.ifNull(defaultValue: () -> String): String {
    return this ?: defaultValue()
}

inline fun String?.ifNullOrBlank(defaultValue: () -> String): String {
    return if (this.isNullOrBlank()) defaultValue() else this
}

fun ZonedDateTime.timestamp(): String {
    return this.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
}