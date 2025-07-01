package io.github.kinasr.playwright_demo_maven.utils.report

import io.github.kinasr.playwright_demo_maven.utils.report.core.TestStep

/**
 * Factory for creating composite test steps
 */
interface CompositeTestStepFactory {
    fun create(steps: List<TestStep>): TestStep
}