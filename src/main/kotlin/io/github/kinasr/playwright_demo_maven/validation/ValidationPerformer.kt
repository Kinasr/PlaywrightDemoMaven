package io.github.kinasr.playwright_demo_maven.validation

import com.microsoft.playwright.Page
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.screenshot.ScreenshotManager
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import io.github.kinasr.playwright_demo_maven.utils.report.Report
import io.github.kinasr.playwright_demo_maven.utils.report.model.AttachmentType
import org.opentest4j.AssertionFailedError
import org.opentest4j.TestAbortedException

class ValidationPerformer(
    val logger: PlayLogger,
    val report: Report
) {

    inline fun guiValidation(
        message: String,
        failureMessage: String,
        screenshot: ScreenshotManager,
        page: Page,
        takeScreenshotOnFailure: Boolean = true,
        operation: () -> Unit
    ): Throwable? {
        val step = report.step(message)

        return runCatching(operation)
            .fold(
                onSuccess = {
                    logger.info { "Validation successful: $message" }
                    step.passed("Validation successful: $message")
                    null
                },
                onFailure = { thr: Throwable ->
                    if (takeScreenshotOnFailure) {
                        screenshot.takeScreenshot(page, message)?.let { image ->
                            step.attach("screenshot", image, AttachmentType.IMAGE_PNG)
                        }
                    }

                    logger.error { "Validation failed: $failureMessage - with error: ${thr.message}" }
                    when (thr) {
                        is AssertionFailedError -> step.failed("Validation failed: $failureMessage", thr.message)
                        is TestAbortedException -> step.skipped("Validation skipped: $message", thr.message)
                        else -> step.broken("Validation broken: $message", thr.message)
                    }
                    thr
                }
            )
    }

    inline fun validation(
        message: String,
        failureMessage: String,
        operation: () -> Unit
    ): Throwable? {
        val step = report.step(message)

        return runCatching(operation)
            .fold(
                onSuccess = {
                    logger.info { "Validation successful: $message" }
                    step.passed("Validation successful: $message")
                    null
                },
                onFailure = { thr: Throwable ->
                    logger.error { "Validation failed: $failureMessage - with error: ${thr.message}" }
                    step.broken("Validation broken: $message", thr.message)
                    thr
                }
            )
    }
}