package io.github.kinasr.playwright_demo_maven.tests

import com.microsoft.playwright.Page
import io.github.kinasr.playwright_demo_maven.config.Config
import io.github.kinasr.playwright_demo_maven.di.PlaywrightTestScope
import io.github.kinasr.playwright_demo_maven.pages.WelcomePage
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.manager.BrowserManager
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.inject
import java.util.*

class WelcomePageTest : KoinTest {
    private val appConfig: Config.App by inject()
    private lateinit var testScope: Scope
    private lateinit var browser: BrowserManager
    private lateinit var page: Page

    @BeforeEach
    fun setup() {
        // Create the test scope
        testScope = getKoin().createScope(
            "test_${UUID.randomUUID()}",
            named(PlaywrightTestScope.TEST_SCOPE)
        )

        // Get browser manager (this is factory scoped, so it's available globally)
        browser = get()

        val context = browser.context {
            this.baseURL = appConfig.baseUrl
        }
        page = context.newPage()

        // Declare the context and page in the test scope so scoped dependencies can access them
        testScope.declare(context, allowOverride = true)
        testScope.declare(page, allowOverride = true)
    }

    @Test
    fun `navigate to AB Testing page`() {
        val welcomePage: WelcomePage = testScope.get()

        welcomePage.navigate()
            .clickABTesting()
            .assertPageTitleContains("A/B Test546")
    }

    @AfterEach
    fun tearDown() {
        browser.quit()
        testScope.close()
    }
}