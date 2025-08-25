package io.github.kinasr.playwright_demo_maven.validation

import com.microsoft.playwright.Locator
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.GUI
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action.element.GUIElementValidation
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.model.GUIElement
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.model.GUIElementI
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import io.github.kinasr.playwright_demo_maven.utils.report.Report

class GUIValidationBuilder(
    logger: PlayLogger,
    report: Report,
    performer: ValidationPerformer
) : ValidationBuilder(logger, report, performer) {

    fun validate(gui: GUI, element: GUIElementI): GUIElementValidation {
        return GUIElementValidation(this, gui, element)
    }

    fun validate(gui: GUI, locator: Locator): GUIElementValidation {
        return GUIElementValidation(this, gui, GUIElement(locator))
    }
}