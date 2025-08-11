package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action

import com.microsoft.playwright.BrowserContext
import com.microsoft.playwright.Locator
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.GUI
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.model.GUIElementI
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.screenshot.ScreenshotManager
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import io.github.kinasr.playwright_demo_maven.utils.report.Report

class GUIElementAction(
    logger: PlayLogger,
    report: Report,
    screenshot: ScreenshotManager,
    context: BrowserContext,
    private val element: GUIElementI
) : GUI(logger, report, screenshot, context) {

    fun click(options: (Locator.ClickOptions.() -> Unit) = { }): GUIElementAction {
        performElementAction("click", element) {
            val op = Locator.ClickOptions().also { it.options() }
            element.locator.click(op)
        }
        return this
    }

    fun fill(text: String, options: (Locator.FillOptions.() -> Unit) = { }): GUIElementAction {
        performElementAction("fill", element) {
            val op = Locator.FillOptions().also { it.options() }
            element.locator.fill(text, op)
        }
        return this
    }
}