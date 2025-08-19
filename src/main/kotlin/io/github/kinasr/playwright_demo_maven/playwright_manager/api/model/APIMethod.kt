package io.github.kinasr.playwright_demo_maven.playwright_manager.api.model

enum class APIMethod(val str: String) {
    HEAD("HEAD"),
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE")
}