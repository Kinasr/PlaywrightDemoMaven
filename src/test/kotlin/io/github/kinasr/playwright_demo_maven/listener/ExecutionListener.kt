package io.github.kinasr.playwright_demo_maven.listener

import io.github.kinasr.playwright_demo_maven.di.logModule
import io.github.kinasr.playwright_demo_maven.di.mainModule
import io.github.kinasr.playwright_demo_maven.di.reportModule
import org.junit.platform.engine.TestExecutionResult
import org.junit.platform.launcher.TestExecutionListener
import org.junit.platform.launcher.TestIdentifier
import org.junit.platform.launcher.TestPlan
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

class ExecutionListener : KoinComponent, TestExecutionListener {

    override fun testPlanExecutionStarted(testPlan: TestPlan?) {
        startKoin {
            modules(mainModule, logModule, reportModule)
        }

//        val report: Report by inject()
//        report.cleanup()
//        report.init(
//            mapOf(
//                "os" to System.getProperty("os.name"),
//                "os.version" to System.getProperty("os.version"),
//                "browser" to Config.Browser().name
//            )
//        )
    }

    override fun executionFinished(testIdentifier: TestIdentifier?, testExecutionResult: TestExecutionResult?) {
//        if (testIdentifier?.isTest == true) {
//            val testStatus = when (testExecutionResult?.status) {
//                TestExecutionResult.Status.SUCCESSFUL -> TestStatus.PASSED
//                TestExecutionResult.Status.FAILED -> TestStatus.FAILED
//                else -> TestStatus.SKIPPED
//            }
//            
//            val report: Report by inject()
//            report.endTest(testIdentifier.displayName, testStatus)
//        }
    }

    override fun testPlanExecutionFinished(testPlan: TestPlan?) {
//        val report: Report by inject()
//        report.generate()
    }
}