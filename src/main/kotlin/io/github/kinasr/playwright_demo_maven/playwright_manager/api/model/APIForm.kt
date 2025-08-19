package io.github.kinasr.playwright_demo_maven.playwright_manager.api.model

import com.microsoft.playwright.options.FilePayload
import com.microsoft.playwright.options.FormData
import java.nio.file.Path

data class APIForm(
    private var fields: MutableList<Pair<String, Any>> = mutableListOf()
) {
    fun set(name: String, value: Any) {
        fields.removeIf { it.first == name }
        append(name, value)
    }

    fun append(name: String, value: Any) {
        fields.add(name to value)
    }

    fun toFormData(): FormData {
        val formData = FormData.create()
        fields.forEach { (key, value) ->
            when (value) {
                is String -> formData.set(key, value)
                is Boolean -> formData.set(key, value)
                is Int -> formData.set(key, value)
                is Path -> formData.set(key, value)
                is FilePayload -> formData.set(key, value)
                else -> throw IllegalArgumentException("Unsupported form data type for key $key: ${value.javaClass}")
            }
        }
        return formData
    }

    fun toPrettyString(): String {
        return fields.toString()
    }
}