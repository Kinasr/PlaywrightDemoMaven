package io.github.kinasr.playwright_demo_maven.config

import org.koin.core.component.KoinComponent

open class Config() : KoinComponent {

    class Playwright(private val playwrightConfig: PlaywrightConfig?) {
        val env: Map<String, String>
            get() = playwrightConfig?.env ?: mapOf()
    }

    class Browser(private val browserConfig: BrowserConfig?) {

        val name: String
            get() = browserConfig?.name ?: "chromium"

        val headless: Boolean
            get() = browserConfig?.headless ?: false

        val timeout: Double
            get() = browserConfig?.timeout ?: 30000.0

        val slowMo: Double
            get() = browserConfig?.slowMo ?: 0.0

        val devtools: Boolean
            get() = browserConfig?.devtools ?: false

        val viewportWidth: Int
            get() = browserConfig?.viewport?.width ?: 1920

        val viewportHeight: Int
            get() = browserConfig?.viewport?.height ?: 1080
    }

    class App(private val appConfig: AppConfig?) {
        val baseUrl: String
            get() = appConfig?.baseUrl ?: throw Exception("App base URL is not set")

        val appTimeout: Int
            get() = appConfig?.timeout ?: 30000
    }

    class Test(private val testConfig: TestConfig?) : Config() {
        val parallel: Boolean
            get() = testConfig?.parallel ?: false

        val threads: Int
            get() = testConfig?.threads ?: 4

        val retries: Int
            get() = testConfig?.retries ?: 2

        val screenshots: Boolean
            get() = testConfig?.screenshots ?: false

        val videos: Boolean
            get() = testConfig?.videos ?: false

        val traces: Boolean
            get() = testConfig?.traces ?: false
    }

    class Logging(private val loggingConfig: LoggingConfig?) {
        val level: String
            get() = loggingConfig?.level ?: "INFO"

        val enablePerformance: Boolean
            get() = loggingConfig?.enablePerformance ?: false

        val enableAPIDebug: Boolean
            get() = loggingConfig?.enableAPIDebug ?: false
    }

    class Allure(private val allureConfig: AllureConfig?) : Config() {
        val resultsDirectory: String
            get() = allureConfig?.resultsDirectory ?: "target/allure-results"
    }
}