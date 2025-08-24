package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action.page

import com.microsoft.playwright.Page
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.GUI

class GUIPageGetter(
    private val gui: GUI,
    private val page: Page
) {

    fun title(): String {
        return gui.performer.action(
            message = "Getting page title",
            failureMessage = "Failed to get page title",
        ) {
            page.title()
        }
    }

    fun screenshot(options: (Page.ScreenshotOptions.() -> Unit) = { }): ByteArray? {
        return gui.performer.action(
            message = "Taking screenshot of page",
            failureMessage = "Failed to take screenshot of page",
        ) {
            val op = Page.ScreenshotOptions().apply(options)
            page.screenshot(op)
        }
    }
}