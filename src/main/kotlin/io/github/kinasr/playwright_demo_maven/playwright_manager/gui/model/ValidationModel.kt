package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.model

data class ValidationModel(
    val message: String,
    val operation: () -> Throwable?
)
