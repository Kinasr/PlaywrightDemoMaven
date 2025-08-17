package io.github.kinasr.playwright_demo_maven.playwright_manager.api

data class APIResult<T>(
    val statusCode: Int,
    val status: String,
    val url: String,
    val headers: Map<String, String>,
    val body: T?,
    val text: String,
    val parseError: String? = null
) {
    val isSuccess = statusCode in 200..299
    val location: String? = headers["Location"] ?: headers["location"]
    val cookies: List<String> = headers.entries
        .filter { it.key.lowercase() == "set-cookie" }
        .map { it.value }
    val contentType: String? = headers["Content-Type"] ?: headers["content-type"]

    fun isJson(): Boolean = contentType?.contains("application/json", ignoreCase = true) == true
    fun isXml(): Boolean = contentType?.contains("xml", ignoreCase = true) == true
    fun isHtml(): Boolean = contentType?.contains("text/html", ignoreCase = true) == true
    
}