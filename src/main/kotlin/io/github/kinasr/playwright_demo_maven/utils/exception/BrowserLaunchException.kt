package io.github.kinasr.playwright_demo_maven.utils.exception

class BrowserLaunchException(
    message: String,
    cause: Throwable
) : RuntimeException(message, cause)