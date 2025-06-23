package io.github.kinasr.playwright_demo_maven.listener

import io.github.kinasr.playwright_demo_maven.di.testModule
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.koin.core.context.startKoin

class JUnitTestListener : BeforeEachCallback {
    override fun beforeEach(context: ExtensionContext) {
        startKoin {
            modules(testModule)
        }
    }
}