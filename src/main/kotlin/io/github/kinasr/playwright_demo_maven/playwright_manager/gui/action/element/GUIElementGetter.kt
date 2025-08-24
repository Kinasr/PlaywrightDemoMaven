package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action.element

import com.microsoft.playwright.Locator
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.GUI
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.model.GUIElementI

class GUIElementGetter(
    private val gui: GUI,
    private val element: GUIElementI
) {

    fun textContent(options: (Locator.TextContentOptions.() -> Unit) = { }): String {
        return gui.performer.action(
            "Getting text content of element '${element.name}'",
            "Failed to get text content of element '${element.name}'"
        ) {
            val op = Locator.TextContentOptions().apply(
                options
            )
            element.locator.textContent(op)
        }
    }

    fun screenshot(options: (Locator.ScreenshotOptions.() -> Unit) = { }): ByteArray? {
        return gui.performer.action(
            "Taking screenshot of element '${element.name}'",
            "Failed to take screenshot of element '${element.name}'"
        ) {
            val op = Locator.ScreenshotOptions().apply(options)
            element.locator.screenshot(op)
        }
    }
}