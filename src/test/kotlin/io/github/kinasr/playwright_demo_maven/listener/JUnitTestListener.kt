package io.github.kinasr.playwright_demo_maven.listener

import io.github.kinasr.playwright_demo_maven.di.mainModule
import io.github.kinasr.playwright_demo_maven.utils.report.Report
import io.github.kinasr.playwright_demo_maven.utils.report.model.TestStatus
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.AfterTestExecutionCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.koin.core.context.startKoin

class JUnitTestListener : BeforeTestExecutionCallback {
    
    override fun beforeTestExecution(context: ExtensionContext?) {
        Report.startTest(context?.displayName ?: "Unknown")
    }
}