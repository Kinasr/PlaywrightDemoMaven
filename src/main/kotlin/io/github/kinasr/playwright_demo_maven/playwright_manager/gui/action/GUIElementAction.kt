package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action

import com.microsoft.playwright.Locator
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.GUI
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.model.GUIElementI
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.validation.GUIElementValidation
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.validation.ValidationBuilder

class GUIElementAction(
    private val gui: GUI,
    private val element: GUIElementI,
    private val validationBuilder: ValidationBuilder
) {

    fun click(options: (Locator.ClickOptions.() -> Unit) = { }): GUIElementAction {
        gui.performElementAction("click", element) {
            val op = Locator.ClickOptions().also { it.options() }
            element.locator.click(op)
        }
        return this
    }

    fun fill(text: String, options: (Locator.FillOptions.() -> Unit) = { }): GUIElementAction {
        gui.performElementAction("fill", element) {
            val op = Locator.FillOptions().also { it.options() }
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