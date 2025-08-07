package io.github.kinasr.playwright_demo_maven.listener

import org.junit.jupiter.api.extension.AfterTestExecutionCallback
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.koin.core.component.KoinComponent

class JUnitTestListener : KoinComponent, BeforeTestExecutionCallback, AfterTestExecutionCallback {

    override fun beforeTestExecution(context: ExtensionContext?) {
//        report.startTest(context?.displayName ?: "Unknown")
    }

    override fun afterTestExecution(context: ExtensionContext?) {
//        report.endTest(context?.displayName ?: "Unknown", TestStatus.PASSED)
    }
}