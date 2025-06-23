package io.github.kinasr.playwright_demo_maven.config

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

open class Config() : KoinComponent {
    protected val configuration: ConfigRecord by inject<ConfigRecord>(named("config"))

    class Browser() : Config() {
        
        val browserConfig: BrowserConfig
            get() = configuration.browser ?: throw Exception("Browser is not set")

        val name: String
            get() = browserConfig.name ?: throw Exception("Browser name is not set")

        val headless: Boolean
            get() = browserConfig.headless ?: false

        val timeout: Double
            get() = browserConfig.timeout ?: 30000.0
        
        val slowMo: Double
            get() = browserConfig.slowMo ?: 0.0
        
        val devtools: Boolean
            get() = browserConfig.devtools ?: false
        
        val viewportWidth: Int
            get() = browserConfig.viewport?.width ?: 1920
        
        val viewportHeight: Int
            get() = browserConfig.viewport?.height ?: 1080
    }
    
    class App() : Config() {
        val baseUrl: String
            get() = configuration.app?.baseUrl ?: throw Exception("App base URL is not set")
        
        val appTimeout: Int
            get() = configuration.app?.timeout ?: 30000
    }
    
    class Test() : Config() {
        val parallel: Boolean
            get() = configuration.test?.parallel ?: false
                
        val threads: Int
            get() = configuration.test?.threads ?: 4
        
        val retries: Int
            get() = configuration.test?.retries ?: 2
        
        val screenshots: Boolean
            get() = configuration.test?.screenshots ?: false
        
        val videos: Boolean
            get() = configuration.test?.videos ?: false
        
        val traces: Boolean
            get() = configuration.test?.traces ?: false
    }
    
    class Logging() : Config() {
        val level: String
            get() = configuration.logging?.level ?: "INFO"
        
        val enablePerformance: Boolean
            get() = configuration.logging?.enablePerformance ?: false
        
        val enableAPIDebug: Boolean
            get() = configuration.logging?.enableAPIDebug ?: false
    }
    
    class Allure() : Config() {
        val resultsDirectory: String
            get() = configuration.allure?.resultsDirectory ?: "target/allure-results"
    }
}