package io.github.kinasr.playwright_demo_maven.listener

import io.github.kinasr.playwright_demo_maven.config.Config
import io.github.kinasr.playwright_demo_maven.di.logModule
import io.github.kinasr.playwright_demo_maven.di.mainModule
import io.github.kinasr.playwright_demo_maven.di.reportModule
import io.github.kinasr.playwright_demo_maven.utils.report.Report
import io.github.kinasr.playwright_demo_maven.utils.report.model.TestStatus
import org.junit.platform.engine.TestExecutionResult
import org.junit.platform.launcher.TestExecutionListener
import org.junit.platform.launcher.TestIdentifier
import org.junit.platform.launcher.TestPlan
import org.koin.core.context.startKoin

class ExecutionListener : TestExecutionListener {

    override fun testPlanExecutionStarted(testPlan: TestPlan?) {
        startKoin {
            modules(mainModule, logModule, reportModule)
        }

        Report.cleanup()
        Report.init(
            mapOf(
                "os" to System.getProperty("os.name"),
                "os.version" to System.getProperty("os.version"),
                "browser" to Config.Browser().name
            )
        )
    }

    override fun executionFinished(testIdentifier: TestIdentifier?, testExecutionResult: TestExecutionResult?) {
        if (testIdentifier?.isTest == true) {
            val testStatus = when (testExecutionResult?.status) {
                TestExecutionResult.Status.SUCCESSFUL -> TestStatus.PASSED
                TestExecutionResult.Status.FAILED -> TestStatus.FAILED
                else -> TestStatus.SKIPPED
            }
            Report.endTest(testIdentifier.displayName, testStatus)
        }
    }

    override fun testPlanExecutionFinished(testPlan: TestPlan?) {
        Report.generate()
    }
}