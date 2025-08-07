package io.github.kinasr.playwright_demo_maven.utils.report_old

import io.github.kinasr.playwright_demo_maven.utils.report_old.core.TestStep

/**
 * Factory for creating composite test steps
 */
interface CompositeTestStepFactory {
    fun create(steps: List<TestStep>): TestStep
}