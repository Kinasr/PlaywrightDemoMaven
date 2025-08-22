package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.screenshot

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page

interface ScreenshotManager {
    fun takeScreenshot(page: Page, actionName: String): ByteArray?
    fun takeElementScreenshot(element: Locator, actionName: String): ByteArray?
}