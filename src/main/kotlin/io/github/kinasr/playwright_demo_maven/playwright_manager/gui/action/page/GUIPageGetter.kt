package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action.page

import com.microsoft.playwright.Page
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.GUI
import jdk.internal.joptsimple.internal.Messages.message

class GUIPageGetter(
    private val gui: GUI,
    private val page: Page
) {
    
    fun title(): String {
        return gui.performer.action(
            message = "Getting page title",
            failureMessage = "Failed to get page title",
        ) {
            page.title()
        }
    }
}