package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action.element

import com.microsoft.playwright.assertions.LocatorAssertions
import com.microsoft.playwright.assertions.PlaywrightAssertions
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.model.GUIElementI
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.validation.Validation
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.validation.ValidationBuilder

class GUIElementValidation(
    builder: ValidationBuilder,
    private val element: GUIElementI
): Validation(builder) {
    override val and = builder
    override val then = builder

    fun hasText(text: String, options: (LocatorAssertions.HasTextOptions.() -> Unit) = { }): GUIElementValidation {
        builder.addValidation {
            builder.performValidation(
                "Element '${element.name}' has text '$text'",
                "Element '${element.name}' does not have text '$text'"
            ) {
                val op = LocatorAssertions.HasTextOptions().also { it.options() }
                PlaywrightAssertions.assertThat(element.locator).hasText(text, op)
            }
        }

        return this
    }

    fun isVisible(options: (LocatorAssertions.IsVisibleOptions.() -> Unit) = { }): GUIElementValidation {
        builder.addValidation {
            builder.performValidation(
                "Element '${element.name}' is visible",
                "Element '${element.name}' is not visible"
            ) {
                val op = LocatorAssertions.IsVisibleOptions().also { it.options() }
                PlaywrightAssertions.assertThat(element.locator).isVisible(op)
            }
        }

        return this
    }
}