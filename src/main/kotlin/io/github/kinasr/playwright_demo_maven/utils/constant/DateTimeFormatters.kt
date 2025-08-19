package io.github.kinasr.playwright_demo_maven.utils.constant

import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

object DateTimeFormatters {
    val ZONED_DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
        .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 9, true)
        .appendPattern("XXX")
        .toFormatter()
        .withZone(ZoneId.of("UTC"))
    
    val ZONED_DATE_TIME_FORMATTER_WITHOUT_DECIMAL: DateTimeFormatter = DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
        .toFormatter()
        .withZone(ZoneId.of("UTC"))
}