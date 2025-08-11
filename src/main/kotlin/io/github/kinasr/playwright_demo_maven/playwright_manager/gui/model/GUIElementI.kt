package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.model

import com.microsoft.playwright.Locator

interface GUIElementI {
    val locator: Locator
    val name: String
        get() = locator.toString()
    val isSingleElement: Boolean
        get() = true
}   