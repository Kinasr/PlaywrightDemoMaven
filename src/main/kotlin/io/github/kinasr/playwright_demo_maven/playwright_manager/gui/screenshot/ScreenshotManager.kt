package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.screenshot

import com.microsoft.playwright.BrowserContext
import com.microsoft.playwright.Locator

interface ScreenshotManager {
    fun takeScreenshot(context: BrowserContext, actionName: String): ByteArray?
    fun takeElementScreenshot(element: Locator, actionName: String): ByteArray?
}