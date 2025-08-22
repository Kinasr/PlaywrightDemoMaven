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
        val worker = Thread.currentThread()
        val workerId = worker.threadId()

        return browserPool.compute(workerId) { _, browser ->
            browser?.let { b ->
                if (b.isConnected) browser
                else {
                    runCatching { closeBrowser(workerId, b) }
                        .onFailure { logger.warn { "Error closing unhealthy browser for thread ${worker.name}: ${it.message}" } }
                    null
                }
            } ?: run {
                logger.debug { "Initializing browser for thread: ${worker.name} (ID: $workerId)" }
                runCatching { initBrowser() }
                    .onSuccess { logger.info { "Browser initialized: ${config.browser.name} for thread: ${worker.name}" } }
                    .onFailure { e ->
                        logger.error { "Failed to initialize browser: ${e.message} for thread: ${worker.name}" }
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
            channel = config.browser.channel
        }
    }

    private fun browserConnectOptions(): BrowserType.ConnectOptions {
        return BrowserType.ConnectOptions().apply {
            slowMo = config.browser.slowMo
            timeout = config.browser.timeout
        }
    }

    @Synchronized
    fun clearPool() {
        logger.info { "Clearing browser pool." }
        browserPool.forEach { workerId, browser ->
            closeBrowser(workerId, browser)
        }
        browserPool.clear()
    }

    private fun closeBrowser(threadId: Long, browser: Browser) {
        runCatching { browser.close() }
            .onSuccess { logger.trace { "Browser closed for thread id: $threadId" } }
            .onFailure { logger.warn { "Error closing browser for thread id: $threadId: ${it.message}" } }
    }

    override fun close() {
        clearPool()
    }
}