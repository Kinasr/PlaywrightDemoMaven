package io.github.kinasr.playwright_demo_maven.playwright_manager.gui

import com.microsoft.playwright.BrowserContext
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.model.GUIElementI
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

    inline fun <T> performElementAction(
        actionName: String,
        element: GUIElementI,
        takeScreenshotOnFailure: Boolean = true,
        action: () -> T
    ): T {
        val singleOrPlural = if (element.isSingleElement) "element" else "elements"
        logger.info {
            "Performing action '$actionName' on $singleOrPlural by '${element.name}'"
        }

        val step = report.step("$actionName on $singleOrPlural by '${element.name}'")
        return try {
            val result = action()
            step.passed()
            result
        } catch (e: Exception) {
            if (takeScreenshotOnFailure) {
                screenshot.takeScreenshot(context, actionName)?.let { image ->
                    step.attach("screenshot", image, AttachmentType.IMAGE_PNG)
                }
            }
            step.failed(
                "Failed to '$actionName' on $singleOrPlural by '${element.name}'",
                e.message
            )
            throw e
        }
    }
}