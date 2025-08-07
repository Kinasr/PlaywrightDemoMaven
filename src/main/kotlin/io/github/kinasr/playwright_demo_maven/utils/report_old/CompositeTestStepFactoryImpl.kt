package io.github.kinasr.playwright_demo_maven.utils.report_old

import io.github.kinasr.playwright_demo_maven.utils.report_old.core.TestStep

class CompositeTestStepFactoryImpl : CompositeTestStepFactory {
    override fun create(steps: List<TestStep>): TestStep {
        return CompositeTestStep(steps)
    }
}