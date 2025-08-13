package io.github.kinasr.playwright_demo_maven.playwright_manager

import com.microsoft.playwright.Playwright
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger

class PlaywrightManager(
    private val logger: PlayLogger
): AutoCloseable {
    @Volatile
    private var playwright: Playwright? = null 
    
    @Synchronized
    fun initialize(options: (Playwright.CreateOptions.() -> Unit) = {}) : Playwright {
        return playwright ?: run {
            logger.info { "Initializing Playwright" }
            val op = Playwright.CreateOptions().also { it.options() }
            playwright = Playwright.create(op)
            playwright!!
        }
    }
    
    @Synchronized
    override fun close() {
        try {
            playwright?.close()
            playwright = null
            
            logger.info { "Playwright closed" }
        } catch (e: Exception) {
            logger.error { "Error closing Playwright: ${e.message}" }
            throw e
        }
    }
}