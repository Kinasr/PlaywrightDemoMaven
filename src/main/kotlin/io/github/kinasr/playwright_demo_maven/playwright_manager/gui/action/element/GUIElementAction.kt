package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action.element

import com.microsoft.playwright.Locator
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.GUI
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.model.GUIElementI
import io.github.kinasr.playwright_demo_maven.validation.GUIValidationBuilder
import io.github.kinasr.playwright_demo_maven.validation.ValidationBuilder

class GUIElementAction(
    private val gui: GUI,
    private val validationBuilder: GUIValidationBuilder,
    private val element: GUIElementI
) {

    fun click(options: (Locator.ClickOptions.() -> Unit) = { }): GUIElementAction {
        gui.performAction(
            message = "Clicking on element '${element.name}'",
            failureMessage = "Failed to click on element '${element.name}'"
        ) {
            val op = Locator.ClickOptions().apply(options)
            element.locator.click(op)
        }
        return this
    }

    fun fill(text: String, options: (Locator.FillOptions.() -> Unit) = { }): GUIElementAction {
        gui.performAction(
            message = "Filling text '$text' in element '${element.name}'",
            failureMessage = "Failed to fill text '$text' in element '${element.name}'"
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
        return validationBuilder.validate(element)
    }
}