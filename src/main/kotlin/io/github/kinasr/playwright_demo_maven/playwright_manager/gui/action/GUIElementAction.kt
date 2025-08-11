package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action

import com.microsoft.playwright.BrowserContext
import com.microsoft.playwright.Locator
import com.microsoft.playwright.options.MouseButton
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.GUI
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.screenshot.ScreenshotManager
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import io.github.kinasr.playwright_demo_maven.utils.report.Report

class GUIElementAction(
    logger: PlayLogger,
    report: Report,
    screenshot: ScreenshotManager,
    context: BrowserContext,
    private val locator: Locator
) : GUI(logger, report, screenshot, context) {

    fun click(options: (Locator.ClickOptions.() -> Unit) = { }): GUIElementAction {
        performElementAction("click", locator) {
            val op = Locator.ClickOptions().also { it.options() }
            locator.click(op)
        }
        return this
    }

    fun xx() {
        click {
            button = MouseButton.RIGHT
            this
        }
    }
    
    fun fill(text: String, options: (Locator.FillOptions.() -> Unit) = { }): GUIElementAction {
        performElementAction("fill", locator) {
            val op = Locator.FillOptions().also { it.options() }
            locator.fill(text, op)
        }
        return this
    }
}