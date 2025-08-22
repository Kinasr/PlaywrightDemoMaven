package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.screenshot

import com.microsoft.playwright.Browser
import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import io.github.kinasr.playwright_demo_maven.utils.timestamp
import java.nio.file.Files
import java.nio.file.Paths
import java.time.ZonedDateTime

class PlayScreenshot(
    private val logger: PlayLogger,
    private val directory: String
) : ScreenshotManager {

    override fun takeScreenshot(page: Page, actionName: String): ByteArray? {
        val filename = "${actionName}_${ZonedDateTime.now().timestamp()}.png"

        return try {
            val path = Paths.get(directory, filename)
            Files.createDirectories(path.parent)

            page.screenshot(Page.ScreenshotOptions().setPath(path))
        } catch (e: Exception) {
            logger.warn { "Failed to take screenshot: ${e.message}" }
            null
        }
    }

    fun x(browser: Browser) {
        browser.contexts().firstOrNull()?.pages()?.firstOrNull()?.screenshot()
    }

    override fun takeElementScreenshot(element: Locator, actionName: String): ByteArray? {
        val filename = "${actionName}_element_${ZonedDateTime.now().timestamp()}.png"

        return try {
            val path = Paths.get(directory, filename)
            Files.createDirectories(path.parent)

            element.screenshot(Locator.ScreenshotOptions().setPath(path))
        } catch (e: Exception) {
            logger.warn { "Failed to take element screenshot: ${e.message}" }
            null
        }
    }

}