package io.github.kinasr.playwright_demo_maven.utils.gson_adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class ZonedDateTimeAdapter(private val formatters: List<DateTimeFormatter>): TypeAdapter<ZonedDateTime>() {
    override fun write(out: JsonWriter?, value: ZonedDateTime?) {
       if (value == null)
           out?.nullValue()
        else
            out?.value(value.toString())
    }

    override fun read(p0: JsonReader?): ZonedDateTime? {
        if (p0?.peek() == JsonToken.NULL) {
            p0.nextNull()
            return null
        } else if (p0?.hasNext() ?: false) {
            val valueStr = p0.nextString()
            formatters.forEach { formatter ->
                try {
                    return ZonedDateTime.parse(valueStr, formatter)
                        .truncatedTo(ChronoUnit.SECONDS)
                } catch (_: Exception) {
                    // Ignore and try the next formatter
                }
            }
        }
        return null
    }
}