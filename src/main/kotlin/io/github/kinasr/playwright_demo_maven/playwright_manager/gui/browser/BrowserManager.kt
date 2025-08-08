package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.browser

import com.microsoft.playwright.Browser
import com.microsoft.playwright.BrowserContext
import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Playwright
import io.github.kinasr.playwright_demo_maven.config.Config
import io.github.kinasr.playwright_demo_maven.utils.logger.LoggerName
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class BrowserManager(private val playwright: Playwright) : KoinComponent {
    private val logger: PlayLogger by inject(named(LoggerName.PLAYWRIGHT))

    @Volatile
    private var browser: Browser? = null
    private val browserContext: ThreadLocal<BrowserContext> = ThreadLocal()

    @Synchronized
    fun getBrowser(): Browser {
        if (browser == null) {
            try {
                browser = initBrowser()
                logger.info { "Browser initialized: ${Config.Browser().name}" }
            } catch (e: Exception) {
                logger.error { "Failed to initialize browser: ${e.message}" }
                throw RuntimeException("Could not initialize browser", e)
            }
        }
        return browser!!
    }

    fun getContext(options: Browser.NewContextOptions = Browser.NewContextOptions()): BrowserContext {
        if (browserContext.get() == null) {
            try {
                browserContext.set(getBrowser().newContext(options))
                logger.info { "New browser context created for thread: ${Thread.currentThread().name}" }
            } catch (e: Exception) {
                logger.error { "Failed to create browser context: ${e.message}" }
                throw RuntimeException("Could not create browser context", e)
            }
        }
        return browserContext.get()!!
    }

    private fun initBrowser(): Browser {
        val options = browserOptions()
        return try {
            when (Config.Browser().name.lowercase()) {
                "firefox" -> playwright.firefox().launch(options)
                "webkit" -> playwright.webkit().launch(options)
                else -> playwright.chromium().launch(options)
            }
        } catch (e: Exception) {
            logger.error { "Error launching browser: ${e.message}" }
            throw e
        }
    }

    private fun browserOptions(): BrowserType.LaunchOptions {
        return BrowserType.LaunchOptions().apply { 
            headless = Config.Browser().headless
            slowMo = Config.Browser().slowMo
        }
    }

    fun close() {
        try {
            browserContext.get()?.let {
                it.close()
                logger.info { "Browser context closed for thread: ${Thread.currentThread().name}" }
            }
        } catch (e: Exception) {
            logger.warn { "Error closing browser context: ${e.message}" }
        } finally {
            browserContext.remove()
        }
    }

    @Synchronized
    fun quit() {
        try {
            close()
            browser?.let {
                it.close()
                logger.info { "Browser closed." }
            }
        } catch (e: Exception) {
            logger.warn { "Error closing browser: ${e.message}" }
        } finally {
            browser = null
        }
    }
}