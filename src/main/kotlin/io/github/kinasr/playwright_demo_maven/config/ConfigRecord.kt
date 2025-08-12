package io.github.kinasr.playwright_demo_maven.config

data class ConfigRecord(
    var playwright: PlaywrightConfig? = null,
    var browser: BrowserConfig? = null,
    var app: AppConfig? = null,
    var test: TestConfig? = null,
    var logging: LoggingConfig? = null,
    var allure: AllureConfig? = null
)

data class PlaywrightConfig(
    var env: Map<String, String>? = null,
)

data class BrowserConfig(
    var name: String? = null,
    var headless: Boolean? = null,
    var timeout: Double? = null,
    var slowMo: Double? = null,
    var devtools: Boolean? = null,
    var viewport: ViewPortConfig? = null
)

data class ViewPortConfig(
    var width: Int? = null,
    var height: Int? = null
)

data class AppConfig(
    var baseUrl: String? = null,
    var timeout: Int? = null
)

data class TestConfig(
    var parallel: Boolean? = null,
    var threads: Int? = null,
    var retries: Int? = null,
    var screenshots: Boolean? = null,
    var videos: Boolean? = null,
    var traces: Boolean? = null
)

data class LoggingConfig(
    var level: String? = null,
    var enablePerformance: Boolean? = null,
    var enableAPIDebug: Boolean? = null
)

data class AllureConfig(
    var resultsDirectory: String? = null
)
