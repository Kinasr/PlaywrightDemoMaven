package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action

import com.microsoft.playwright.Locator
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import io.github.kinasr.playwright_demo_maven.utils.report.Report

class GUIElementAction(
    private val logger: PlayLogger,
    private val report: Report,
    private val locator: Locator
): Locator by locator {
    
    fun click(options: (Locator.ClickOptions.() -> Unit)): GUIElementAction {
        return this
    }
}