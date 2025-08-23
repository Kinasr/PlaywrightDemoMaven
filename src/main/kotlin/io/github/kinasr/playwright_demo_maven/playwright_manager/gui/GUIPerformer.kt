package io.github.kinasr.playwright_demo_maven.playwright_manager.gui

import com.microsoft.playwright.Page
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.screenshot.ScreenshotManagerI
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import io.github.kinasr.playwright_demo_maven.utils.report.Report
import io.github.kinasr.playwright_demo_maven.utils.report.model.AttachmentType

class GUIPerformer(
    val logger: PlayLogger,
    val report: Report,
    val screenshot: ScreenshotManagerI,
    val page: Page
) {

    inline fun <T> action(
        message: String,
        failureMessage: String = message,
        takeScreenshotOnFailure: Boolean = true,
        action: () -> T
    ): T {
        logger.info { message }
        val step = report.step(message)

        return try {
            val result = action()
            step.passed()
            result
        } catch (e: Exception) {
            if (takeScreenshotOnFailure) {
                screenshot.takeScreenshot(page, message.replace(" ", "_"))
                    ?.let { image ->
                        step.attach("screenshot", image, AttachmentType.IMAGE_PNG)
                    }
            }
            logger.error { "$failureMessage - with error: ${e.message}" }
            step.failed(failureMessage, e.message)
            throw e
        }
    }
    
}