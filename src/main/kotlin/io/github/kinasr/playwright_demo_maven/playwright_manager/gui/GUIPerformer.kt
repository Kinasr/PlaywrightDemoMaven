package io.github.kinasr.playwright_demo_maven.playwright_manager.gui

import com.microsoft.playwright.Page
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import io.github.kinasr.playwright_demo_maven.utils.report.Report
import io.github.kinasr.playwright_demo_maven.utils.report.model.AttachmentType

class GUIPerformer(
    val logger: PlayLogger,
    val report: Report
) {

    inline fun <T> action(
        message: String,
        failureMessage: String = message,
        gui: GUI? = null,
        page: Page? = null,
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
            if (gui != null && page != null && takeScreenshotOnFailure) {
                gui.page(page).get().screenshot()?.let {
                    step.attach("screenshot", it, AttachmentType.IMAGE_PNG)
                }
            }
            logger.error { "$failureMessage - with error: ${e.message}" }
            step.failed(failureMessage, e.message)
            throw e
        }
    }

}