package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action.page

import com.microsoft.playwright.Page
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.GUI
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.validation.ValidationBuilder
import jdk.internal.joptsimple.internal.Messages.message

class GUIPageAction(
    private val gui: GUI,
    private val validationBuilder: ValidationBuilder,
    private val page: Page
) {

    fun navigate(url: String = "", options: (Page.NavigateOptions.() -> Unit) = { }): GUIPageAction {
        val urlMsg = url.ifBlank { "BaseURL" }
        gui.performAction(
            message = "Navigating to $urlMsg",
            failureMessage = "Failed to navigate to $urlMsg",
        ) {
            val op = Page.NavigateOptions().apply(options)
            page.navigate(url, op)
        }
        return this
    }
}