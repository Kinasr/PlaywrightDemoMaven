package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.screenshot

import com.microsoft.playwright.Locator
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import io.github.kinasr.playwright_demo_maven.utils.timestamp
import java.nio.file.Files
import java.nio.file.Paths
import java.time.ZonedDateTime

class LocatorScreenshotManager(
    private val logger: PlayLogger,
    private val locator: Locator,
    private val directory: String
) : ScreenshotManager {
    override fun capture(actionName: String): ByteArray? {
        return capture(actionName) {}
    }

    fun capture(actionName: String, options: (Locator.ScreenshotOptions.() -> Unit)): ByteArray? {
        val filename = "${actionName}_element_${ZonedDateTime.now().timestamp()}.png"

        return runCatching {
            val path = Paths.get(directory, filename)
            Files.createDirectories(path.parent)

            val op = Locator.ScreenshotOptions().apply {
                setPath(path)
                options
            }
            locator.screenshot(op)
        }.onSuccess {
            logger.trace { "Screenshot taken for $actionName: $filename" }
        }.onFailure {
            logger.warn { "Failed to take screenshot: ${it.message}" }
        }.getOrNull()
    }
}