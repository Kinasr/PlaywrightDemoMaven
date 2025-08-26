package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.model

import com.microsoft.playwright.Locator

data class GUIElement(
    override val locator: Locator,
    override val name: String = locator.toString(),
    override val isSingleElement: Boolean = true
) : GUIElementI
