package io.github.kinasr.playwright_demo_maven.utils.report

import io.github.kinasr.playwright_demo_maven.utils.report.core.TestStep

class CompositeTestStepFactoryImpl : CompositeTestStepFactory {
    override fun create(steps: List<TestStep>): TestStep {
        return CompositeTestStep(steps)
    }
}