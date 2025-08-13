package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.manager

import com.microsoft.playwright.Browser
import com.microsoft.playwright.BrowserContext
import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Playwright
import io.github.kinasr.playwright_demo_maven.config.Config
import io.github.kinasr.playwright_demo_maven.utils.exception.BrowserLaunchException
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import org.koin.core.component.KoinComponent

class BrowserManager(
    private val logger: PlayLogger,
    private val browserConfig: Config.Browser,
    private val playwright: Playwright,
    private val contextOptions: (Browser.NewContextOptions.() -> Unit) = { }
) : KoinComponent {

    @Volatile
    private var browser: Browser? = null
    private val browserContext: ThreadLocal<BrowserContext> = ThreadLocal()

    @Synchronized
    private fun getBrowser(): Browser {
        if (browser == null) {
            try {
                browser = initBrowser()
                logger.info { "Browser initialized: ${browserConfig.name}" }
            } catch (e: Exception) {
                logger.error { "Failed to initialize browser: ${e.message}" }
                throw e
            }
        }
        return browser!!
    }

    fun context(): BrowserContext {
        if (browserContext.get() == null) {
            try {
                val op = Browser.NewContextOptions().also { it.contextOptions() }
                browserContext.set(getBrowser().newContext(op))
                logger.info { "New browser context created for thread: ${Thread.currentThread().name}" }
            } catch (e: Exception) {
                logger.error { "Failed to create browser context: ${e.message}" }
                throw e
            }
        }
        return browserContext.get()!!
    }

    private fun initBrowser(): Browser {
        val options = browserOptions()
        return try {
            when (browserConfig.name.lowercase()) {
                "firefox" -> playwright.firefox().launch(options)
                "webkit" -> playwright.webkit().launch(options)
                "chrome", "chromium" -> playwright.chromium().launch(browserOptions())
                else -> {
                    logger.warn { "Unknown browser '${browserConfig.name}', defaulting to Chromium" }
                    playwright.chromium().launch(browserOptions())
                }
            }
        } catch (e: Exception) {
            logger.error { "Failed to launch ${browserConfig.name}: ${e.message}" }
            throw BrowserLaunchException("Could not initialize ${browserConfig.name} browser", e)
        }
    }

    private fun browserOptions(): BrowserType.LaunchOptions {
        return BrowserType.LaunchOptions().apply {
            headless = browserConfig.headless
            slowMo = browserConfig.slowMo
        }
    }

    fun close() {
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

    @Synchronized
    fun quit() {
        try {
            close()
            browser?.close()
            logger.info { "Browser closed." }
        } catch (e: Exception) {
            logger.error { "Error closing browser: ${e.message}" }
            throw e
        } finally {
            browser = null
        }
    }
}