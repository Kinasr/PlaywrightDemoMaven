package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action.browser

import com.microsoft.playwright.BrowserContext
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.GUI
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action.page.GUIPageAction
import io.github.kinasr.playwright_demo_maven.utils.exception.GUIException

class GUIBrowserAction(
    private val gui: GUI,
    private val context: BrowserContext
) {

    fun focusOnNewPage(action: () -> Unit, options: (BrowserContext.WaitForPageOptions.() -> Unit) = {}): GUIPageAction {
        val newPage = gui.performAction(
            message = "",
            failureMessage = ""
        ) {
            val op = BrowserContext.WaitForPageOptions().apply(options)
            context.waitForPage(op, action)
        }

        return gui.page(newPage).focus()
    }
    
    fun focusOnPage(title: String): GUIPageAction {
        val page = gui.performAction(
            message = "Focusing on page with title '$title'",
            failureMessage = "Failed to focus on page with title '$title'"
        ) {
            context.pages().firstOrNull { it.title() == title }
                ?: throw GUIException("Page with title '$title' not found")
        }
        
        return gui.page(page).focus()
    }
}