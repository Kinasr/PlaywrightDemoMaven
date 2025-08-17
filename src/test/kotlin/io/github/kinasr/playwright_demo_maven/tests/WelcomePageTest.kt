package io.github.kinasr.playwright_demo_maven.tests

import com.microsoft.playwright.Browser
import com.microsoft.playwright.Page
import io.github.kinasr.playwright_demo_maven.config.Config
import io.github.kinasr.playwright_demo_maven.di.PlaywrightTestScope
import io.github.kinasr.playwright_demo_maven.pages.WelcomePage
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.manager.BrowserContextManager
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.inject
import java.util.*

class WelcomePageTest : KoinTest {
    private val config: Config by inject()
    private lateinit var testScope: Scope
    private lateinit var browserContext: BrowserContextManager
    private lateinit var page: Page

    @BeforeEach
    fun setup() {
        testScope = getKoin().createScope(
            "test_${UUID.randomUUID()}",
            named(PlaywrightTestScope.TEST_SCOPE)
        )

        browserContext = get<BrowserContextManager> {
            parametersOf(
                Browser.NewContextOptions().apply {
                    this.baseURL = config.app.baseUrl
                }
            )
        }

        val context = browserContext.context()
        page = context.newPage()

        testScope.declare(context, allowOverride = true)
        testScope.declare(page, allowOverride = true)
    }

    @Test
    fun `navigate to AB Testing page`() {
        val welcomePage: WelcomePage = testScope.get()

        welcomePage.navigate()
            .clickABTesting()
            .assertPageTitleContains("A/B Test")
    }

    @AfterEach
    fun tearDown() {
        browserContext.close()
        testScope.close()
    }
}