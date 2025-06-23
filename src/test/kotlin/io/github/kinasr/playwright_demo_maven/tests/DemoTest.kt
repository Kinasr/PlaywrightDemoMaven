package io.github.kinasr.playwright_demo_maven.tests

import io.github.kinasr.playwright_demo_maven.di.testModule
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin

class DemoTest {
    
    @Test
    fun main() {
        startKoin {
            modules(testModule)
        }
        
        println("Hello World")
    }
}