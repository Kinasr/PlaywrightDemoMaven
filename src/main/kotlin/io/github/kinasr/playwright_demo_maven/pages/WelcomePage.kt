package io.github.kinasr.playwright_demo_maven.pages

import com.microsoft.playwright.Page
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.GUI

class WelcomePage(
    gui: GUI,
    page: Page
): WebPage(gui, page) {
    
    private val linkABTesting = page.getByText("A/B Testing")
    
    fun navigate(): WelcomePage {
        gui.page(page).navigate("")
        return this
    }
    
    fun clickABTesting(): ABTestingPage {
        gui.element(linkABTesting).click()
        return ABTestingPage(gui, page)
    }

}