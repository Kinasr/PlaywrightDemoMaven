package io.github.kinasr.playwright_demo_maven.listener

import io.github.kinasr.playwright_demo_maven.utils.report_old.Report
import org.junit.jupiter.api.extension.AfterTestExecutionCallback
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class JUnitTestListener : KoinComponent, BeforeTestExecutionCallback, AfterTestExecutionCallback {
    private val report: Report by inject()

    override fun beforeTestExecution(context: ExtensionContext?) {
//        report.startTest(context?.displayName ?: "Unknown")
    }

    override fun afterTestExecution(context: ExtensionContext?) {
//        report.endTest(context?.displayName ?: "Unknown", TestStatus.PASSED)
    }
}