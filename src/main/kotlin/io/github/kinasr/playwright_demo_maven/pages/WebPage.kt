package io.github.kinasr.playwright_demo_maven.pages

import com.microsoft.playwright.Page
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.GUI

abstract class WebPage(
    val gui: GUI,
    val page: Page
) {
}