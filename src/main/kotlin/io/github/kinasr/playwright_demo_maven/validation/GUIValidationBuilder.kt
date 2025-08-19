package io.github.kinasr.playwright_demo_maven.validation

import com.microsoft.playwright.BrowserContext
import com.microsoft.playwright.Locator
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action.element.GUIElementValidation
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.model.GUIElement
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.model.GUIElementI
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.screenshot.ScreenshotManager
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import io.github.kinasr.playwright_demo_maven.utils.report.Report

class GUIValidationBuilder(
    logger: PlayLogger,
    report: Report,
    performer: ValidationPerformer,
    val screenshotManager: ScreenshotManager,
    val context: BrowserContext
) : ValidationBuilder(logger, report, performer) {

    fun validate(element: GUIElementI): GUIElementValidation {
        return GUIElementValidation(this, element, screenshotManager, context)
    }
    
    fun validate(locator: Locator): GUIElementValidation {
        return GUIElementValidation(this, GUIElement(locator), screenshotManager, context)
    }
}