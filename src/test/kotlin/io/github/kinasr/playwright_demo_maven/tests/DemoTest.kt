package io.github.kinasr.playwright_demo_maven.tests

import io.github.kinasr.playwright_demo_maven.di.mainModule
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin

class DemoTest {
    
    @Test
    fun main() {
        startKoin {
            modules(mainModule)
        }
        
        println("Hello World")
    }
}