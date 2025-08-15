package io.github.kinasr.playwright_demo_maven.listener

import io.github.kinasr.playwright_demo_maven.di.mainModule
import io.github.kinasr.playwright_demo_maven.playwright_manager.PlaywrightManager
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.manager.BrowserManager
import org.junit.platform.launcher.TestExecutionListener
import org.junit.platform.launcher.TestPlan
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class ExecutionListener : KoinComponent, TestExecutionListener {

    override fun testPlanExecutionStarted(testPlan: TestPlan?) {
        startKoin {
            modules(mainModule)
        }
    }

    override fun testPlanExecutionFinished(testPlan: TestPlan?) {
        get<BrowserManager>().close()
        get<PlaywrightManager>().close()
        stopKoin()
    }
}