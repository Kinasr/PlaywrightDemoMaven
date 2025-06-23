package io.github.kinasr.playwright_demo_maven.browser

import com.microsoft.playwright.*
import io.github.kinasr.playwright_demo_maven.config.Config
import io.qameta.allure.kotlin.Step
import org.slf4j.LoggerFactory

class BrowserManager {
    private val logger = LoggerFactory.getLogger(BrowserManager::class.java)

    private var playwright: Playwright? = null
    private var browser: Browser? = null
    private var context: BrowserContext? = null
    private var page: Page? = null

    @Step("Initialize browser")
    fun initializeBrowser(): Page {
        logger.info("Initializing browser: ${Config.Browser().name}")

        playwright = Playwright.create()

        val browserType = when (Config.Browser().name.lowercase()) {
            "firefox" -> playwright!!.firefox()
            "webkit" -> playwright!!.webkit()
            else -> playwright!!.chromium()
        }

        val launchOptions = BrowserType.LaunchOptions()
            .setHeadless(Config.Browser().headless)
            .setSlowMo(Config.Browser().slowMo)
            .setDevtools(Config.Browser().devtools)

        browser = browserType.launch(launchOptions)

        val contextOptions = Browser.NewContextOptions()
            .setViewportSize(Config.Browser().viewportWidth, Config.Browser().viewportHeight)

        if (Config.Test().videos) {
            contextOptions.setRecordVideoDir(java.nio.file.Paths.get("target/videos"))
        }

        context = browser!!.newContext(contextOptions)

        if (Config.Test().traces) {
            context!!.tracing().start(Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true))
        }

        page = context!!.newPage()
        page!!.setDefaultTimeout(Config.Browser().timeout)

        logger.info("Browser initialized successfully")
        return page!!
    }

    @Step("Close browser")
    fun closeBrowser() {
        logger.info("Closing browser")

        if (Config.Test().traces && context != null) {
            context!!.tracing().stop(Tracing.StopOptions()
                .setPath(java.nio.file.Paths.get("target/traces/trace.zip")))
        }

        page?.close()
        context?.close()
        browser?.close()
        playwright?.close()

        logger.info("Browser closed successfully")
    }

    @Step("Take screenshot")
    fun takeScreenshot(name: String = "screenshot"): ByteArray? {
        return try {
            page?.screenshot(Page.ScreenshotOptions()
                .setPath(java.nio.file.Paths.get("target/screenshots/$name.png"))
                .setFullPage(true))
        } catch (e: Exception) {
            logger.error("Failed to take screenshot: ${e.message}")
            null
        }
    }

    fun getPage(): Page? = page
    fun getContext(): BrowserContext? = context
    fun getBrowser(): Browser? = browser
}