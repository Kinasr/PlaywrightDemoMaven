package io.github.kinasr.playwright_demo_maven.pages

import com.microsoft.playwright.Page
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.GUI

abstract class WebPage(
    protected val gui: GUI,
    protected val page: Page
) {
}