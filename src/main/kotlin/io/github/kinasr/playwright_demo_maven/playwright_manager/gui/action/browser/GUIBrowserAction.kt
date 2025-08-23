package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action.browser

import com.microsoft.playwright.BrowserContext
import com.microsoft.playwright.options.Cookie
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.GUI
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action.page.GUIPageAction
import io.github.kinasr.playwright_demo_maven.utils.exception.GUIException

class GUIBrowserAction(
    private val gui: GUI,
    private val context: BrowserContext
) {

    fun focusOnNewPage(
        action: () -> Unit,
        options: (BrowserContext.WaitForPageOptions.() -> Unit) = {}
    ): GUIPageAction {
        val newPage = gui.performer.action(
            message = "Waiting for new page to open",
            failureMessage = "Failed to wait for new page to open"
        ) {
            val op = BrowserContext.WaitForPageOptions().apply(options)
            context.waitForPage(op, action)
        }

        return gui.page(newPage).focus()
    }

    fun focusOnPage(title: String): GUIPageAction {
        val page = gui.performer.action(
            message = "Focusing on page with title '$title'",
            failureMessage = "Failed to focus on page with title '$title'"
        ) {
            context.pages().firstOrNull { it.title() == title }
                ?: throw GUIException("Page with title '$title' not found")
        }

        return gui.page(page).focus()
    }
    
    fun addCookies(cookies: List<Cookie>): GUIBrowserAction {
        gui.performer.action(
            message = "Adding cookies",
            failureMessage = "Failed to add cookies"
        ) {
            context.addCookies(cookies)
        }
        return this
    }
}