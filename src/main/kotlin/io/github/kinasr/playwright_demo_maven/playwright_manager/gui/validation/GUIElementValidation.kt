package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.validation

import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.model.GUIElementI

class GUIElementValidation(
    private val builder: ValidationBuilder,
    private val element: GUIElementI
) {

    fun hasText(text: String): ValidationBuilder {
        builder.addValidation {
            builder.performValidation(
                "Element '${element.name}' has text '$text'",
                "Element '${element.name}' does not have text '$text'"
            ) {
                assertThat(element.locator).hasText(text)
            }
        }

        return builder
    }

    fun isVisible(): ValidationBuilder {
        builder.addValidation {
            builder.performValidation(
                "Element '${element.name}' is visible",
                "Element '${element.name}' is not visible"
            ) {
                assertThat(element.locator).isVisible()
            }
        }

        return builder
    }
}