package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.manager

import com.microsoft.playwright.Browser
import com.microsoft.playwright.BrowserContext
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import java.io.Closeable

class BrowserContextManager(
    private val logger: PlayLogger,
    private val browser: Browser,
    private val contextOptions: Browser.NewContextOptions = Browser.NewContextOptions()
) : Closeable {
    private val browserContext: ThreadLocal<BrowserContext> = ThreadLocal()

    fun context(): BrowserContext {
        if (browserContext.get() == null) {
            try {
                browserContext.set(browser.newContext(contextOptions))
                logger.info { "New browser context created for thread: ${Thread.currentThread().name}" }
            } catch (e: Exception) {
                logger.error { "Failed to create browser context: ${e.message}" }
                throw e
            }
        }
        return browserContext.get()!!
    }

    override fun close() {
        try {
            browserContext.get()?.close()
            logger.info { "Browser context closed for thread: ${Thread.currentThread().name}" }
        } catch (e: Exception) {
            logger.error { "Error closing browser context: ${e.message}" }
            throw e
        } finally {
            browserContext.remove()
        }
    }
}