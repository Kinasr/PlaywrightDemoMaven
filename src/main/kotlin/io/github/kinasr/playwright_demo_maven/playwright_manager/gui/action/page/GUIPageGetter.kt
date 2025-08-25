package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action.page

import com.microsoft.playwright.Page
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.GUI

class GUIPageGetter(
    private val gui: GUI,
    private val page: Page
) {

    fun title(): String {
        return action(
            "Getting page title",
            "Failed to get page title",
        ) {
            page.title()
        }
    }

    fun screenshot(options: (Page.ScreenshotOptions.() -> Unit) = { }): ByteArray? {
        return action(
            "Taking screenshot of page",
            "Failed to take screenshot of page",
        ) {
            val op = Page.ScreenshotOptions().apply(options)
            page.screenshot(op)
        }
    }

    private fun <T> action(msg: String, fMsg: String, operation: () -> T): T {
        return gui.performer.action(
            msg, fMsg, gui, page
        ) { operation() }
    }
}