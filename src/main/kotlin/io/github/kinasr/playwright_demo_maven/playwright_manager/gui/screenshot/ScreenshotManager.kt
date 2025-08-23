package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.screenshot

interface ScreenshotManager {
    fun capture(actionName: String): ByteArray?
}