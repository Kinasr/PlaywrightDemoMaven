package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action.page

import com.microsoft.playwright.Page
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.GUI
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.validation.ValidationBuilder

class GUIPageAction(
    private val gui: GUI,
    private val validationBuilder: ValidationBuilder,
    private val page: Page
) {
    
    fun navigate(url: String, options: (Page.NavigateOptions.() -> Unit) = { }): GUIPageAction {
        gui.performAction(
            message = "Navigating to $url",
            failureMessage = "Failed to navigate to $url",
        ) {
            val op = Page.NavigateOptions().also { it.options() }
            page.navigate(url, op)
        }
        return this
    }
}