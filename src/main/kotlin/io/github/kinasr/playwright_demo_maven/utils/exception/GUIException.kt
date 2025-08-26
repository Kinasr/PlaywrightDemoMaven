package io.github.kinasr.playwright_demo_maven.utils.exception

class GUIException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)