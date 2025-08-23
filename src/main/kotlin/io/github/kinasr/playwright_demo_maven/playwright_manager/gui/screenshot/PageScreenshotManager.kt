package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.screenshot

import com.microsoft.playwright.Page
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import io.github.kinasr.playwright_demo_maven.utils.timestamp
import java.nio.file.Files
import java.nio.file.Paths
import java.time.ZonedDateTime

class PageScreenshotManager(
    private val logger: PlayLogger,
    private val page: Page,
    private val directory: String
) : ScreenshotManager {

    override fun capture(actionName: String): ByteArray? {
        return capture(actionName) {}
    }

    fun capture(actionName: String, options: (Page.ScreenshotOptions.() -> Unit)): ByteArray? {
        val filename = "${actionName}_${ZonedDateTime.now().timestamp()}.png"

        return runCatching {
            val path = Paths.get(directory, filename)
            Files.createDirectories(path.parent)

            val op = Page.ScreenshotOptions().apply {
                setPath(path)
                options
            }
            page.screenshot(op)
        }.onSuccess {
            logger.trace { "Screenshot taken for $actionName: $filename" }
        }.onFailure {
            logger.warn { "Failed to take screenshot: ${it.message}" }
        }.getOrNull()
    }
}