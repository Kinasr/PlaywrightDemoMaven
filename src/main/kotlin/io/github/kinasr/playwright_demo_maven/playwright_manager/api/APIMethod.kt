package io.github.kinasr.playwright_demo_maven.playwright_manager.api

import com.microsoft.playwright.APIRequestContext
import com.microsoft.playwright.APIResponse
import com.microsoft.playwright.options.RequestOptions

enum class APIMethod(val str: String) {
    HEAD("HEAD"),
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE")
}