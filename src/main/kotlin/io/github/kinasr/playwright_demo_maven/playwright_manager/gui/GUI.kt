package io.github.kinasr.playwright_demo_maven.playwright_manager.gui

import com.microsoft.playwright.BrowserContext
import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action.browser.GUIBrowserAction
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action.element.GUIElementAction
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action.page.GUIPageAction
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.model.GUIElement
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.model.GUIElementI
import io.github.kinasr.playwright_demo_maven.validation.GUIValidationBuilder

open class GUI(
    internal val performer: GUIPerformer,
    internal val validationBuilder: GUIValidationBuilder,
    internal val context: BrowserContext
) {

    fun element(element: GUIElementI) = GUIElementAction(this, element)

    fun element(locator: Locator): GUIElementAction {
        return GUIElementAction(this, GUIElement(locator))
    }

    fun page(page: Page): GUIPageAction {
        return GUIPageAction(this, page)
    }
    
    fun browser(context: BrowserContext? = null): GUIBrowserAction {
        return GUIBrowserAction(this, context ?: this.context)
    }
}