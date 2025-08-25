package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action.element

import com.microsoft.playwright.Locator
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.GUI
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.model.GUIElementI
import io.github.kinasr.playwright_demo_maven.validation.GUIValidationBuilder

class GUIElementAction(
    private val gui: GUI,
    private val element: GUIElementI,
    private val validationBuilder: GUIValidationBuilder
) {

    fun click(options: (Locator.ClickOptions.() -> Unit) = { }): GUIElementAction {
        action(
            "Clicking on element '${element.name}'",
            "Failed to click on element '${element.name}'"
        ) {
            val op = Locator.ClickOptions().apply(options)
            element.locator.click(op)
        }
        return this
    }

    fun fill(text: String, options: (Locator.FillOptions.() -> Unit) = { }): GUIElementAction {
        action(
            "Filling text '$text' in element '${element.name}'",
            "Failed to fill text '$text' in element '${element.name}'"
        ) {
            val op = Locator.FillOptions().apply(options)
            element.locator.fill(text, op)
        }
        return this
    }

    fun get(): GUIElementGetter {
        return GUIElementGetter(gui, element)
    }

    fun validate(): GUIElementValidation {
        return validationBuilder.validate(gui, element)
    }
    
    private fun <T> action(msg: String, fMsg: String, operation: () -> T): T {
        return gui.performer.action(
            msg, fMsg, gui, element.locator.page()
        ) { operation() }
    }
}