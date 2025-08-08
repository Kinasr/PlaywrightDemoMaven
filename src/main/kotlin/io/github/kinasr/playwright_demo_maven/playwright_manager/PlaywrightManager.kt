package io.github.kinasr.playwright_demo_maven.playwright_manager

import com.microsoft.playwright.Playwright

class PlaywrightManager() {
    @Volatile
    private var playwright: Playwright? = null 
    
    @Synchronized
    fun initialize() : Playwright {
        return playwright ?: run { 
            playwright = Playwright.create()
            playwright!!
        }
        
        TODO("Add option from config")
    }
    
    @Synchronized
    fun close() {
        playwright?.close()
        playwright = null
    }
}