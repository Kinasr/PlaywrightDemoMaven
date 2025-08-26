package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action.element

import com.microsoft.playwright.assertions.LocatorAssertions
import com.microsoft.playwright.assertions.PlaywrightAssertions
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.GUI
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.model.GUIElementI
import io.github.kinasr.playwright_demo_maven.validation.Validation
import io.github.kinasr.playwright_demo_maven.validation.ValidationBuilder

class GUIElementValidation(
    builder: ValidationBuilder,
    private val gui: GUI,
    private val element: GUIElementI,
) : Validation(builder) {
    override val and = builder
    override val then = builder

    fun hasText(text: String, options: (LocatorAssertions.HasTextOptions.() -> Unit) = { }): GUIElementValidation {
        builder.addValidation {
            validate(
                "Element '${element.name}' has text '$text'",
                "Element '${element.name}' does not have text '$text'"
            ) {
                val op = LocatorAssertions.HasTextOptions().apply(options)
                PlaywrightAssertions.assertThat(element.locator).hasText(text, op)
            }
        }

        return this
    }

    fun containsText(
        text: String,
        msg: Pair<String, String> = "Element '${element.name}' has text '$text'" to "Element '${element.name}' does not have text '$text'",
        options: (LocatorAssertions.ContainsTextOptions.() -> Unit) = { }
    ): GUIElementValidation {
        builder.addValidation {
           validate(msg.first, msg.second) {
                val op = LocatorAssertions.ContainsTextOptions().apply(options)
                PlaywrightAssertions.assertThat(element.locator).containsText(text, op)
            }
        }

        return this
    }

    fun isVisible(options: (LocatorAssertions.IsVisibleOptions.() -> Unit) = { }): GUIElementValidation {
        builder.addValidation {
            validate(
                "Element '${element.name}' is visible",
                "Element '${element.name}' is not visible",
            ) {
                val op = LocatorAssertions.IsVisibleOptions().apply(options)
                PlaywrightAssertions.assertThat(element.locator).isVisible(op)
            }
        }

        return this
    }

    private fun validate(msg: String, fMsg: String, operation: () -> Unit): Throwable? {
        return builder.performer.locatorValidation(
            gui, element.locator, msg, fMsg
        ) { operation() }
    }
}