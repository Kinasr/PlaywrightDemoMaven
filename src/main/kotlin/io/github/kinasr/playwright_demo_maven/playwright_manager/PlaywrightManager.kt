package io.github.kinasr.playwright_demo_maven.playwright_manager

import com.microsoft.playwright.Playwright

class PlaywrightManager(): AutoCloseable {
    @Volatile
    private var playwright: Playwright? = null 
    
    @Synchronized
    fun initialize(options: (Playwright.CreateOptions.() -> Unit) = {}) : Playwright {
        return playwright ?: run {
            val op = Playwright.CreateOptions().also { it.options() }
            playwright = Playwright.create(op)
            playwright!!
        }
    }
    
    @Synchronized
    override fun close() {
        playwright?.close()
        playwright = null
    }
}