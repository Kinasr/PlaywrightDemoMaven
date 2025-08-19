package io.github.kinasr.playwright_demo_maven.playwright_manager.gui

import com.microsoft.playwright.BrowserContext
import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action.element.GUIElementAction
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action.page.GUIPageAction
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.model.GUIElement
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.model.GUIElementI
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.screenshot.ScreenshotManager
import io.github.kinasr.playwright_demo_maven.validation.ValidationBuilder
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import io.github.kinasr.playwright_demo_maven.utils.report.Report
import io.github.kinasr.playwright_demo_maven.utils.report.model.AttachmentType
import io.github.kinasr.playwright_demo_maven.validation.GUIValidationBuilder

open class GUI(
    val logger: PlayLogger,
    val report: Report,
    val screenshot: ScreenshotManager,
    val context: BrowserContext,
    private val validationBuilder: GUIValidationBuilder
) {

    fun element(element: GUIElementI) = GUIElementAction(this, validationBuilder, element)

    fun element(locator: Locator): GUIElementAction {
        return GUIElementAction(this, validationBuilder, GUIElement(locator))
    }
    
    fun page(page: Page): GUIPageAction {
        return GUIPageAction(this, validationBuilder, page)
    }

    inline fun <T> performAction(
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
                screenshot.takeScreenshot(context, message.replace(" ", "_"))
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