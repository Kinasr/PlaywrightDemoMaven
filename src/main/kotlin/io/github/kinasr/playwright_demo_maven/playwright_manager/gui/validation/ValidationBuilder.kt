package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.validation

import com.microsoft.playwright.BrowserContext
import com.microsoft.playwright.Locator
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.model.GUIElement
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.model.GUIElementI
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.screenshot.ScreenshotManager
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import io.github.kinasr.playwright_demo_maven.utils.report.Report
import io.github.kinasr.playwright_demo_maven.utils.report.model.AttachmentType
import org.opentest4j.AssertionFailedError
import org.opentest4j.TestAbortedException

class ValidationBuilder(
    val logger: PlayLogger,
    val report: Report,
    val screenshot: ScreenshotManager,
    val context: BrowserContext,
) {
    private val validations: MutableList<() -> Throwable?> = mutableListOf()

    fun validate(element: GUIElementI): GUIElementValidation {
        return GUIElementValidation(this, element)
    }

    fun validate(locator: Locator): GUIElementValidation {
        return GUIElementValidation(this, GUIElement(locator))
    }

    fun assertAll() {
        val step = report.step("Assert all validations")
        validations.forEach { validation ->
            val throwable = validation()

            if (throwable != null) {
                logger.error { "Assertion failed: ${throwable.message}" }
                step.failed()
                throw throwable
            }
        }

        logger.info { "All assertions passed" }
        step.passed()
    }

    fun verifyAll() {
        val step = report.step("Verify all validations")
        var isPassed = true
        
        validations.forEach { validation ->
           isPassed = validation() == null && isPassed
        }
        
       if (isPassed) {
           logger.info { "All verifications passed" }
           step.passed()
       } else {
           logger.error { "Verification failed" }
           step.failed()
           throw AssertionFailedError("Verification failed")
       }
    }

    internal fun addValidation(validation: () -> Throwable?) {
        validations.add(validation)
    }

    inline fun performValidation(
        message: String,
        failureMessage: String,
        takeScreenshotOnFailure: Boolean = true,
        operation: () -> Unit
    ): Throwable? {
        val step = report.step("")

        return runCatching(operation)
            .fold(
                onSuccess = {
                    logger.info { "Validation successful: $message" }
                    step.passed("Validation successful: $message")
                    null
                },
                onFailure = { thr: Throwable ->
                    if (takeScreenshotOnFailure) {
                        screenshot.takeScreenshot(context, message)?.let { image ->
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
}