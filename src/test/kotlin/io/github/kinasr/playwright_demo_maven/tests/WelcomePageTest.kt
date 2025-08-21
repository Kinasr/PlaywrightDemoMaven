package io.github.kinasr.playwright_demo_maven.tests

import com.microsoft.playwright.Browser
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
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

//        browserContext = get<BrowserContextManager> {
//            parametersOf(
//                Browser.NewContextOptions().apply {
//                    this.baseURL = config.app.baseUrl
//                }
//            )
//        }

//        browserContext = get<BrowserContextManager>()
//
//        val context = browserContext.context()
//        page = context.newPage()
//
//        testScope.declare(context, allowOverride = true)
//        testScope.declare(page, allowOverride = true)
    }

    @Test
    fun `navigate to AB Testing page`() {
        val welcomePage: WelcomePage = get()

        welcomePage.navigate()
            .clickABTesting()
            .assertPageTitleContains("A/B Test")
    }

    @Test
    fun `navigate to different url`() {
        testScope = getKoin().createScope(
            "test_${UUID.randomUUID()}",
            named(PlaywrightTestScope.TEST_SCOPE)
        )

        testScope.declare(
            instance = get<BrowserContextManager> {
            parametersOf(
                Browser.NewContextOptions().apply {
                    this.baseURL = "https://google.com"
                }
            )
        }, allowOverride = true)
        
        val page: Page = testScope.get()
        page.navigate("/")
        
        Thread.sleep(2000)
        testScope.close()
    }
    
    @Test
    fun ttt() {
        Playwright.create().use { playwright ->
            // Use the URL from the command line output
            val wsUrl = "ws://127.0.0.1:51159/devtools/browser/f8f94d39-6617-4340-bd20-94d3c909b0b4"

            // Connect to the existing browser
            val browser: Browser = playwright.chromium().connect(wsUrl)

            // Now you can create a new page in the connected browser
            val page = browser.newPage()
            page.navigate("https://www.google.com")
            println("Page title: " + page.title())
        }
    }

    @AfterEach
    fun tearDown() {
//        browserContext.close()
        testScope.close()
    }
    
}