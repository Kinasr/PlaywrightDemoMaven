package io.github.kinasr.playwright_demo_maven.listener

import io.github.kinasr.playwright_demo_maven.di.configModule
import io.github.kinasr.playwright_demo_maven.di.logModule
import io.github.kinasr.playwright_demo_maven.di.mainModule
import io.github.kinasr.playwright_demo_maven.di.playwrightModule
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
            modules(mainModule)
        }
    }
}