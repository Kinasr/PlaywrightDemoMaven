package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action.page

import com.microsoft.playwright.Page
import com.microsoft.playwright.assertions.PageAssertions
import com.microsoft.playwright.assertions.PlaywrightAssertions
import io.github.kinasr.playwright_demo_maven.validation.Validation
import io.github.kinasr.playwright_demo_maven.validation.ValidationBuilder

class GUIPageValidation(
    builder: ValidationBuilder,
    private val page: Page,
) : Validation(builder) {
    override val and: ValidationBuilder = builder
    override val then: ValidationBuilder = builder

    fun hasTitle(title: String, options: (PageAssertions.HasTitleOptions.() -> Unit) = { }): GUIPageValidation {
        builder.addValidation {
            builder.performValidation(
                "Page title is '${title}'",
                "Page title is not '${title}'"
            ) {
                val op = PageAssertions.HasTitleOptions().apply(options)
                PlaywrightAssertions.assertThat(page).hasTitle(title, op)
            }
        }
        return this
    }

    fun hasURL(url: String, options: (PageAssertions.HasURLOptions.() -> Unit) = { }): GUIPageValidation {
        builder.addValidation {
            builder.performValidation(
                "Page URL is '$url'",
                "Page URL is not '$url'"
            ) {
                val op = PageAssertions.HasURLOptions().apply(options)
                PlaywrightAssertions.assertThat(page).hasURL(url, op)
            }
        }
        return this
    }
}