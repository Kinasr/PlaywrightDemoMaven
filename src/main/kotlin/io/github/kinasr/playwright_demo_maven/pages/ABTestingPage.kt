package io.github.kinasr.playwright_demo_maven.pages

import com.microsoft.playwright.Page
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.GUI

class ABTestingPage(
    gui: GUI,
    page: Page
): WebPage(gui, page) {
    
    private val textPageTitle = page.locator("h3")

    fun navigate(): ABTestingPage {
        gui.page(page).navigate("/abtest")
        return this
    }
    
    fun assertPageTitleContains(title: String) {
        gui.element(textPageTitle).validate()
            .containsText(title).then.assert()
    }
}