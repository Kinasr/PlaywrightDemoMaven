package io.github.kinasr.playwright_demo_maven.playwright_manager.gui

import com.microsoft.playwright.BrowserContext
import com.microsoft.playwright.Locator
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.screenshot.ScreenshotManager
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import io.github.kinasr.playwright_demo_maven.utils.report.Report
import io.github.kinasr.playwright_demo_maven.utils.report.model.AttachmentType

open class GUI(
    val logger: PlayLogger,
    val report: Report,
    val screenshot: ScreenshotManager,
    val context: BrowserContext
) {

    inline fun performElementAction(
        actionName: String,
        locator: Locator,
        multiElement: Boolean = false,
        takeScreenshotOnFailure: Boolean = true,
        action: () -> Unit
    ) {
        logger.info {
            "Performing action '$actionName' on ${if (multiElement) "elements" else "element"} by '$locator'"
        }

        val step = report.step("$actionName on ${if (multiElement) "elements" else "element"} by '$locator'")
        try {
            action()
            step.passed()
        } catch (e: Exception) {
            if (takeScreenshotOnFailure) {
                screenshot.takeScreenshot(context, actionName)?.let { image ->
                    step.attach("screenshot", image, AttachmentType.IMAGE_PNG)
                }
            }
            step.failed(
                "Failed to '$actionName' on ${if (multiElement) "elements" else "element"} by '$locator'",
                e.message
            )
            throw e
        }
    }
}