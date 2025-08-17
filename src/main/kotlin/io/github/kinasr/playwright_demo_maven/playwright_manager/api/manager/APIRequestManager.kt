package io.github.kinasr.playwright_demo_maven.playwright_manager.api.manager

import com.microsoft.playwright.APIRequest
import com.microsoft.playwright.APIRequestContext
import com.microsoft.playwright.Playwright
import io.github.kinasr.playwright_demo_maven.config.Config
import io.github.kinasr.playwright_demo_maven.utils.ifNullOrBlank
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import java.io.Closeable

class APIRequestManager(
    private val logger: PlayLogger,
    private val appConfig: Config.App,
    private val playwright: Playwright,
    private val contextOptions: APIRequest.NewContextOptions
) : Closeable {
    val request: APIRequestContext
    val baseURL = contextOptions.baseURL!!
    
    init {
        contextOptions()
        request = runCatching { playwright.request().newContext(contextOptions) }
            .onSuccess { logger.info { "New API request context created." } }
            .onFailure {
                logger.error { "Failed to create API request context: ${it.message}" }
                throw it
            }
            .getOrThrow()
    }
    
    private fun contextOptions(): APIRequest.NewContextOptions {
        return contextOptions.apply {
            baseURL.ifNullOrBlank { appConfig.baseUrl }
        }
    }

    override fun close() {
        try {
            request.dispose()
            logger.info { "API request context closed." }
        } catch (e: Exception) {
            logger.warn { "Error closing API request context: ${e.message}" }
        }
    }
}