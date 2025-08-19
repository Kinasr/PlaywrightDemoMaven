package io.github.kinasr.playwright_demo_maven.playwright_manager.api.manager

import com.microsoft.playwright.APIRequest
import com.microsoft.playwright.APIRequestContext
import com.microsoft.playwright.Playwright
import io.github.kinasr.playwright_demo_maven.config.Config
import io.github.kinasr.playwright_demo_maven.utils.ifNull
import io.github.kinasr.playwright_demo_maven.utils.ifNullOrBlank
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import java.io.Closeable

class APIRequestManager(
    private val logger: PlayLogger,
    private val config: Config,
    private val playwright: Playwright,
    private val contextOptions: APIRequest.NewContextOptions
) : Closeable {
    private var _requestContext: APIRequestContext? = null

    init {
        contextOptions.apply {
            this.baseURL = this.baseURL.ifNullOrBlank { config.app.baseUrl }
            this.timeout = this.timeout.ifNull { config.api.timeout }
            this.extraHTTPHeaders = this.extraHTTPHeaders.ifNull { config.api.headers }
            this.maxRedirects = this.maxRedirects.ifNull { config.api.maxRedirects }
        }
    }

    val request: APIRequestContext
        get() {
            _requestContext = _requestContext.ifNull {
                runCatching { playwright.request().newContext(contextOptions) }
                    .onSuccess { logger.info { "New API request context created." } }
                    .onFailure {
                        logger.error { "Failed to create API request context: ${it.message}" }
                        throw it
                    }
                    .getOrThrow()
            }
            return _requestContext!!
        }
    val baseURL: String
        get() = contextOptions.baseURL

    override fun close() {
        try {
            _requestContext?.dispose()
            _requestContext = null
            logger.info { "API request context closed." }
        } catch (e: Exception) {
            logger.warn { "Error closing API request context: ${e.message}" }
        }
    }
}