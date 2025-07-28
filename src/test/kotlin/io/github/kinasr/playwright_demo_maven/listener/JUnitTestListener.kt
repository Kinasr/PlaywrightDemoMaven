package io.github.kinasr.playwright_demo_maven.listener

import io.github.kinasr.playwright_demo_maven.utils.report.Report
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class JUnitTestListener : KoinComponent, BeforeTestExecutionCallback {
    private val report: Report by inject()

    override fun beforeTestExecution(context: ExtensionContext?) {
        report.startTest(context?.displayName ?: "Unknown")
    }
}