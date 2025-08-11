package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action

import com.microsoft.playwright.Locator
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.GUI
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.model.GUIElementI

class GUIElementGetter(
    private val gui: GUI,
    private val element: GUIElementI
) {
    
    fun textContent(options: (Locator.TextContentOptions.() -> Unit) = { }): String {
        return gui.performElementAction("get text", element) {
            val op = Locator.TextContentOptions().also { it.options() }
            element.locator.textContent(op)
        }
    }
}