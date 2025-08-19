package io.github.kinasr.playwright_demo_maven.utils.gson_adapter

import com.google.gson.JsonParseException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class DoubleAdapter : TypeAdapter<Double>() {
    override fun write(p0: JsonWriter?, p1: Double?) {
        if (p1 == null)
            p0?.nullValue()
        else
            p0?.value(p1)
    }

    override fun read(p0: JsonReader?): Double? {
        val peek = p0?.peek()

        return when (peek) {
            JsonToken.NULL -> {
                p0.nextNull()
                null
            }

            JsonToken.STRING -> {
                val valueStr = p0.nextString()

                if (valueStr.isNullOrBlank()) null
                else valueStr.toDouble()
            }

            JsonToken.NUMBER -> {
                p0.nextDouble()
            }

            else -> {
                throw JsonParseException("Unexpected token: $peek")
            }
        }
    }
}