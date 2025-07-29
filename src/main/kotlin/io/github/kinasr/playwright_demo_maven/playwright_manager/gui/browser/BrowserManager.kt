package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.browser

import com.microsoft.playwright.Browser
import com.microsoft.playwright.BrowserContext
import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import io.github.kinasr.playwright_demo_maven.config.Config
import io.github.kinasr.playwright_demo_maven.utils.logger.LoggerName
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.getValue

class BrowserManager(private val playwright: Playwright) : KoinComponent {
    private val logger: PlayLogger by inject(named(LoggerName.PLAYWRIGHT))
    
    @Volatile
    private var browser : Browser? = null
    private val browserContext : ThreadLocal<BrowserContext> = ThreadLocal()
    
    @Synchronized
    fun getBrowser() : Browser {
        if (browser == null) {
            browser = initBrowser()
        }
        return browser!!
    }
    
    fun getContext(options : Browser.NewContextOptions = Browser.NewContextOptions()) : BrowserContext {
        if (browserContext.get() == null) {
            browserContext.set(getBrowser().newContext(options))
        }
        return browserContext.get()
    }

    private fun initBrowser() : Browser {
        val options = BrowserType.LaunchOptions()
        
        return when (Config.Browser().name.lowercase()) {
            "firefox" -> playwright.firefox().launch(options)
            "webkit" -> playwright.webkit().launch(options)
            else -> playwright.chromium().launch(options)
        }
    }
    
    private fun browserOptions() : BrowserType.LaunchOptions {
        return BrowserType.LaunchOptions()
            .setHeadless(Config.Browser().headless)
            .setSlowMo(Config.Browser().slowMo)
    }
    
    fun close() {
        browserContext.get().close()
        browser?.close()
    }
}