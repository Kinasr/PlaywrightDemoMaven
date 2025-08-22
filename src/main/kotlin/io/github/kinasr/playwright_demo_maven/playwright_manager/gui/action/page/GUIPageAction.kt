package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action.page

import com.microsoft.playwright.Page
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.GUI
import io.github.kinasr.playwright_demo_maven.validation.ValidationBuilder
import jdk.internal.joptsimple.internal.Messages.message

class GUIPageAction(
    private val gui: GUI,
    private val page: Page
) {

    fun navigate(url: String = "", options: (Page.NavigateOptions.() -> Unit) = { }): GUIPageAction {
        val urlMsg = url.ifBlank { "BaseURL" }
        gui.performer.action(
            message = "Navigating to $urlMsg",
            failureMessage = "Failed to navigate to $urlMsg",
        ) {
            val op = Page.NavigateOptions().apply(options)
            page.navigate(url, op)
        }
        return this
    }
    
    fun focus(): GUIPageAction {
        val pageTitle = this.get().title()
        
        gui.performer.action(
            message = "Focusing on page $pageTitle",
            failureMessage = "Failed to focus on page $pageTitle",
        ) {
            page.bringToFront()
        }
        return this
    }

    fun get(): GUIPageGetter {
        return GUIPageGetter(gui, page)
    }

    fun validate(): GUIPageValidation {
        return gui.validationBuilder.validate(page)
    }
}