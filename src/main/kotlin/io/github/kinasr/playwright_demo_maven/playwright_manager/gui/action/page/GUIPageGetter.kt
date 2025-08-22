package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action.page

import com.microsoft.playwright.Page
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.GUI

class GUIPageGetter(
    private val gui: GUI,
    private val page: Page
) {
    
    fun title(): String {
        return gui.performAction(
            message = "Getting page title",
            failureMessage = "Failed to get page title",
        ) {
            page.title()
        }
    }
}