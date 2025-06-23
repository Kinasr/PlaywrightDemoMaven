package io.github.kinasr.playwright_demo_maven.listener

import org.junit.platform.launcher.TestExecutionListener
import org.junit.platform.launcher.TestPlan

class ExecutionListener : TestExecutionListener {
    override fun testPlanExecutionStarted(testPlan: TestPlan?) {
//        System.setProperty("junit.jupiter.extensions.autodetection.enabled", "true")
    }
}