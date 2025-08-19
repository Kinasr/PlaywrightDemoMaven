package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action.element

import com.microsoft.playwright.BrowserContext
import com.microsoft.playwright.assertions.LocatorAssertions
import com.microsoft.playwright.assertions.PlaywrightAssertions
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.model.GUIElementI
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.screenshot.ScreenshotManager
import io.github.kinasr.playwright_demo_maven.validation.Validation
import io.github.kinasr.playwright_demo_maven.validation.ValidationBuilder
import io.github.kinasr.playwright_demo_maven.validation.ValidationPerformer

class GUIElementValidation(
    builder: ValidationBuilder,
    private val element: GUIElementI,
    private val screenshotManager: ScreenshotManager,
    private val context: BrowserContext
): Validation(builder) {
    override val and = builder
    override val then = builder

    fun hasText(text: String, options: (LocatorAssertions.HasTextOptions.() -> Unit) = { }): GUIElementValidation {
        builder.addValidation {
            builder.performer.guiValidation(
                "Element '${element.name}' has text '$text'",
                "Element '${element.name}' does not have text '$text'",
                screenshotManager,
                context
            ) {
                val op = LocatorAssertions.HasTextOptions().apply(options)
                PlaywrightAssertions.assertThat(element.locator).hasText(text, op)
            }
        }

        return this
    }

    fun containsText(text: String, options: (LocatorAssertions.ContainsTextOptions.() -> Unit) = { }): GUIElementValidation {
        builder.addValidation {
            builder.performer.guiValidation(
                "Element '${element.name}' has text '$text'",
                "Element '${element.name}' does not have text '$text'",
                screenshotManager,
                context
            ) {
                val op = LocatorAssertions.ContainsTextOptions().apply(options)
                PlaywrightAssertions.assertThat(element.locator).containsText(text, op)
            }
        }

        return this
    }

    fun isVisible(options: (LocatorAssertions.IsVisibleOptions.() -> Unit) = { }): GUIElementValidation {
        builder.addValidation {
            builder.performer.guiValidation(
                "Element '${element.name}' is visible",
                "Element '${element.name}' is not visible",
                screenshotManager,
                context
            ) {
                val op = LocatorAssertions.IsVisibleOptions().apply(options)
                PlaywrightAssertions.assertThat(element.locator).isVisible(op)
            }
        }

        return this
    }
}