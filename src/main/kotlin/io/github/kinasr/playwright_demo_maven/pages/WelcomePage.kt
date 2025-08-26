package io.github.kinasr.playwright_demo_maven.pages

import com.microsoft.playwright.Page
import com.microsoft.playwright.options.Cookie
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.GUI

class WelcomePageFactory(
    gui: GUI
) : WebPageFactory(gui) {
    override fun navigate(page: Page?): WelcomePage {
        val finalPage = page ?: gui.browser().newPage()

        gui.page(finalPage).navigate()
        return WelcomePage(gui, finalPage)
    }

    override fun addCookies(cookies: List<Cookie>): WelcomePageFactory {
        super.addCookies(cookies)
        return this
    }
}

class WelcomePage(
    gui: GUI,
    page: Page
) : WebPage(gui, page) {

    private val linkABTesting = page.getByText("A/B Testing")

    fun clickABTesting(): ABTestingPage {
        gui.element(linkABTesting).click()
        return ABTestingPage(gui, page)
    }
}