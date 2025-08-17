package io.github.kinasr.playwright_demo_maven.config

open class Config(
    protected val config: ConfigRecord
) {
    val playwright: PlaywrightConfig by lazy {
        PlaywrightConfig(config)
    }

    val browser: BrowserConfig by lazy {
        BrowserConfig(config)
    }

    val api: APIConfig by lazy {
        APIConfig(config)
    }

    val app: AppConfig by lazy {
        AppConfig(config)
    }

    val test: TestConfig by lazy {
        TestConfig(config)
    }

    val logging: LoggingConfig by lazy {
        LoggingConfig(config)
    }

    val allure: AllureConfig by lazy {
        AllureConfig(config)
    }

    class PlaywrightConfig internal constructor(config: ConfigRecord) {
        val env: Map<String, String> by lazy {
            config.playwright?.env ?: emptyMap()
        }
    }

    class BrowserConfig internal constructor(config: ConfigRecord) {

        val name: String by lazy {
            getEnvOrDefault(
                "BROWSER_NAME",
                config.browser?.name,
                "chromium"
            )
        }

        val headless: Boolean by lazy {
            getEnvOrDefault(
                "BROWSER_HEADLESS",
                config.browser?.headless,
                false
            )
        }

        val slowMo: Double by lazy {
            getEnvOrDefault(
                "BROWSER_SLOW_MO",
                config.browser?.slowMo,
                0.0
            )
        }

        val devtools: Boolean by lazy {
            getEnvOrDefault(
                "BROWSER_DEVTOOLS",
                config.browser?.devtools,
                false
            )
        }

        val viewportWidth: Int by lazy {
            getEnvOrDefault(
                "BROWSER_VIEWPORT_WIDTH",
                config.browser?.viewport?.width,
                1920
            )
        }

        val viewportHeight: Int by lazy {
            getEnvOrDefault(
                "BROWSER_VIEWPORT_HEIGHT",
                config.browser?.viewport?.height,
                1080
            )
        }
    }

    class APIConfig internal constructor(config: ConfigRecord) {
        val headers: Map<String, String> by lazy {
            config.api?.headers ?: emptyMap()
        }

        val timeout: Double by lazy {
            getEnvOrDefault(
                "API_TIMEOUT",
                config.api?.timeout,
                30000.0
            )
        }

        val maxRedirects: Int by lazy {
            getEnvOrDefault(
                "API_MAX_REDIRECTS",
                config.api?.maxRedirects,
                5
            )
        }

        val maxRetries: Int by lazy {
            getEnvOrDefault(
                "API_MAX_RETRIES",
                config.api?.maxRetries,
                0
            )
        }
    }

    class AppConfig internal constructor(config: ConfigRecord) {
        val baseUrl: String by lazy {
            getEnvOrDefault(
                "APP_BASE_URL",
                config.app?.baseUrl,
                "https://the-internet.herokuapp.com/"
            )
        }
    }

    class TestConfig internal constructor(config: ConfigRecord) {
        val parallel: Boolean by lazy {
            getEnvOrDefault(
                "TEST_PARALLEL",
                config.test?.parallel,
                true
            )
        }

        val threads: Int by lazy {
            getEnvOrDefault(
                "TEST_THREADS",
                config.test?.threads,
                4
            )
        }

        val retries: Int by lazy {
            getEnvOrDefault(
                "TEST_RETRIES",
                config.test?.retries,
                2
            )
        }

        val screenshots: Boolean by lazy {
            getEnvOrDefault(
                "TEST_SCREENSHOT",
                config.test?.screenshots,
                true
            )
        }

        val videos: Boolean by lazy {
            getEnvOrDefault(
                "TEST_VIDEO",
                config.test?.videos,
                true
            )
        }

        val traces: Boolean by lazy {
            getEnvOrDefault(
                "TEST_TRACES",
                config.test?.traces,
                true
            )
        }
    }

    class LoggingConfig internal constructor(config: ConfigRecord) {
        val level: String by lazy {
            getEnvOrDefault(
                "LOGGING_LEVEL",
                config.logging?.level,
                "INFO"
            )
        }

        val enablePerformance: Boolean by lazy {
            getEnvOrDefault(
                "LOGGING_ENABLE_PERFORMANCE",
                config.logging?.enablePerformance,
                false
            )
        }

        val enableAPIDebug: Boolean by lazy {
            getEnvOrDefault(
                "LOGGING_ENABLE_API_DEBUG",
                config.logging?.enableAPIDebug,
                false
            )
        }
    }

    class AllureConfig internal constructor(config: ConfigRecord) {
        val directory: String by lazy {
            getEnvOrDefault(
                "ALLURE_DIRECTORY",
                config.allure?.resultsDirectory,
                "/allure-results"
            )
        }
    }
}