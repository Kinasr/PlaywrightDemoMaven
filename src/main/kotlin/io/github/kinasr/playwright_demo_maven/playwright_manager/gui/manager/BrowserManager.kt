package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.manager

import com.microsoft.playwright.Browser
import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Playwright
import io.github.kinasr.playwright_demo_maven.config.Config
import io.github.kinasr.playwright_demo_maven.utils.exception.BrowserLaunchException
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import org.koin.core.component.KoinComponent
import java.io.Closeable
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class BrowserManager(
    private val logger: PlayLogger,
    private val config: Config,
    private val playwright: Playwright
) : KoinComponent, Closeable {

    private val browserPool: ConcurrentMap<Long, Browser> = ConcurrentHashMap()

    fun browser(): Browser {
        val thr = Thread.currentThread()
        val currentThreadId = thr.threadId()

        return browserPool.compute(currentThreadId) { _, browser ->
            browser?.let { b ->
                if (b.isConnected) browser
                else {
                    runCatching { closeBrowser(currentThreadId, b) }
                        .onFailure { logger.warn { "Error closing unhealthy browser for thread ${thr.name}: ${it.message}" } }
                    null
                }
            } ?: run {
                logger.debug { "Initializing browser for thread: ${thr.name} (ID: $currentThreadId)" }
                runCatching { initBrowser() }
                    .onSuccess { logger.info { "Browser initialized: ${config.browser.name} for thread: ${thr.name}" } }
                    .onFailure { e ->
                        logger.error { "Failed to initialize browser: ${e.message} for thread: ${thr.name}" }
                        throw e
                    }
                    .getOrNull()
            }
        }!!
    }

    private fun initBrowser(): Browser {
        return try {
            when (config.browser.name.lowercase()) {
                "firefox" -> launchOrConnectBrowser(playwright.firefox())
                "webkit" -> launchOrConnectBrowser(playwright.webkit())
                "chrome", "chromium" -> launchOrConnectBrowser(playwright.chromium())
                else -> {
                    logger.warn { "Unknown browser '${config.browser.name}', defaulting to Chromium" }
                    launchOrConnectBrowser(playwright.chromium())
                }
            }
        } catch (e: Exception) {
            logger.error { "Failed to launch ${config.browser.name}: ${e.message}" }
            throw BrowserLaunchException("Could not initialize ${config.browser.name} browser", e)
        }
    }

    private fun launchOrConnectBrowser(browserType: BrowserType): Browser {
        if (config.browser.websocketEndpoint.isNotBlank()) {
            return browserType.connect(config.browser.websocketEndpoint, browserConnectOptions())
        }
        return browserType.launch(browserLaunchOptions())
    }

    private fun browserLaunchOptions(): BrowserType.LaunchOptions {
        return BrowserType.LaunchOptions().apply {
            headless = config.browser.headless
            slowMo = config.browser.slowMo
            timeout = config.browser.timeout
        }
    }

    private fun browserConnectOptions(): BrowserType.ConnectOptions {
        return BrowserType.ConnectOptions().apply { 
            slowMo = config.browser.slowMo
            timeout = config.browser.timeout
        }
    }

    @Synchronized
    fun clearBrowserPool() {
        logger.info { "Clearing browser pool." }
        browserPool.forEach { browser ->
            closeBrowser(browser.key, browser.value)
        }
        browserPool.clear()
    }

    private fun closeBrowser(threadId: Long, browser: Browser) {
        runCatching { browser.close() }
            .onSuccess { logger.trace { "Browser closed for thread id: $threadId" } }
            .onFailure { logger.warn { "Error closing browser for thread id: $threadId: ${it.message}" } }
    }

    override fun close() {
        clearBrowserPool()
    }
}