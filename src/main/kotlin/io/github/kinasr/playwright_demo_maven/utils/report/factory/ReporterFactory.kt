package io.github.kinasr.playwright_demo_maven.utils.report.factory

import io.github.kinasr.playwright_demo_maven.utils.report.allure.AllureTestReporter
import io.github.kinasr.playwright_demo_maven.utils.report.core.TestReporter
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Factory for creating and managing test reporters
 */
object ReporterFactory : KoinComponent {
    private val allureReporter: AllureTestReporter by inject()

    fun getActiveReporters(): List<TestReporter> {
        return listOf(allureReporter)
    }

    fun getReporter(type: ReporterType): TestReporter? {
        return when (type) {
            ReporterType.ALLURE -> allureReporter
            // Add other reporter types here
        }
    }
}