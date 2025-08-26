package io.github.kinasr.playwright_demo_maven.pages

import com.microsoft.playwright.Page
import com.microsoft.playwright.options.Cookie
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.GUI

abstract class WebPageFactory(
    val gui: GUI
) {
    abstract fun navigate(page: Page? = null): WebPage

    open fun addCookies(cookies: List<Cookie>): WebPageFactory {
        gui.browser().addCookies(cookies)
        return this
    }
}

abstract class WebPage(
    val gui: GUI,
    val page: Page
) {
}