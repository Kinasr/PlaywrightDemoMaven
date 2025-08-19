package io.github.kinasr.playwright_demo_maven.playwright_manager

import com.google.gson.Gson
import com.microsoft.playwright.APIResponse
import io.github.kinasr.playwright_demo_maven.playwright_manager.api.model.APIResult
import io.github.kinasr.playwright_demo_maven.validation.ValidationBuilder

fun <T> APIResponse.result(type: Class<T>, jsonParser: Gson, validationBuilder: ValidationBuilder): APIResult<T> {
    var parseError: String? = null
    val parsedBody = try {
        jsonParser.fromJson(this.text(), type)
    } catch (e: Exception) {
        parseError = e.message
        null
    }
    return APIResult(
        statusCode = this.status(),
        status = this.statusText(),
        url = this.url(),
        headers = this.headers(),
        body = parsedBody,
        text = this.text(),
        validationBuilder = validationBuilder,
        parseError = parseError
    )
}