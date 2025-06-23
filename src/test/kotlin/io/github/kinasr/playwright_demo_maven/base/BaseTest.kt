package io.github.kinasr.playwright_demo_maven.base

import io.github.kinasr.playwright_demo_maven.browser.BrowserManager
import io.github.kinasr.playwright_demo_maven.config.Config
import io.github.kinasr.playwright_demo_maven.di.testModule
import io.github.kinasr.playwright_demo_maven.utils.ScreenshotHelper
import io.qameta.allure.Allure
import io.qameta.allure.Step
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.junit5.KoinTestExtension
import org.slf4j.LoggerFactory

@ExtendWith(KoinTestExtension::class)
abstract class BaseTest : KoinTest {
    protected val logger = LoggerFactory.getLogger(this::class.java)

    protected val browserManager: BrowserManager by inject()
    protected val screenshotHelper: ScreenshotHelper by inject()

    @BeforeEach
    @Step("Setup test environment")
    open fun setUp(testInfo: TestInfo) {
        logger.info("Setting up test: ${testInfo.displayName}")

        // Start Koin DI
        startKoin {
            modules(testModule)
        }

        // Initialize browser
        browserManager.initializeBrowser()

        // Add test info to Allure
        Allure.getLifecycle().updateTestCase { testCase ->
            testCase.name = testInfo.displayName
            testCase.description = "Test: ${testInfo.displayName}"
        }

        logger.info("Test setup completed: ${testInfo.displayName}")
    }

    @AfterEach
    @Step("Cleanup test environment")
    open fun tearDown(testInfo: TestInfo) {
        logger.info("Tearing down test: ${testInfo.displayName}")

        try {
            // Take screenshot on test completion
            screenshotHelper.takeScreenshot("test_completion_${testInfo.displayName}")

            // Close browser
            browserManager.closeBrowser()

        } catch (e: Exception) {
            logger.error("Error during test teardown: ${e.message}", e)
        } finally {
            // Stop Koin DI
            stopKoin()
            logger.info("Test teardown completed: ${testInfo.displayName}")
        }
    }

    @Step("Handle test failure")
    protected fun handleTestFailure(testInfo: TestInfo, exception: Throwable) {
        logger.error("Test failed: ${testInfo.displayName}", exception)

        // Take screenshot on failure
        screenshotHelper.takeScreenshotOnFailure(testInfo.displayName)

        // Attach error details to Allure
        screenshotHelper.attachTextToAllure("Error Details", exception.stackTraceToString())

        // Attach page source if available
        try {
            val pageSource = browserManager.getPage()?.content()
            pageSource?.let {
                screenshotHelper.attachHtmlToAllure("Page Source", it)
            }
        } catch (e: Exception) {
            logger.warn("Could not attach page source: ${e.message}")
        }
    }

    @Step("Navigate to application")
    protected fun navigateToApplication() {
        logger.info("Navigating to application")
        browserManager.getPage()?.let { page ->
            page.navigate(Config.App().baseUrl)
            page.waitForLoadState()
        }
    }

    @Step("Wait for page load")
    protected fun waitForPageLoad() {
        browserManager.getPage()?.waitForLoadState()
    }
}