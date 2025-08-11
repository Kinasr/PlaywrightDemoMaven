package io.github.kinasr.playwright_demo_maven.utils

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun ZonedDateTime.timestamp(): String {
    return this.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
}