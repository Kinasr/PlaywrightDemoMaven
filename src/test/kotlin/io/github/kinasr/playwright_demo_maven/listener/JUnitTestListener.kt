package io.github.kinasr.playwright_demo_maven.listener

import org.junit.jupiter.api.extension.AfterTestExecutionCallback
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.stopKoin

class JUnitTestListener : KoinComponent, BeforeTestExecutionCallback, AfterTestExecutionCallback {

    override fun beforeTestExecution(context: ExtensionContext?) {
    }

    override fun afterTestExecution(context: ExtensionContext?) {
    }
}