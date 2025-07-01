package io.github.kinasr.playwright_demo_maven.utils.report

import io.github.kinasr.playwright_demo_maven.utils.report.core.TestStep
import io.github.kinasr.playwright_demo_maven.utils.report.model.AttachmentType
import io.github.kinasr.playwright_demo_maven.utils.report.model.TestStatus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent.inject
import java.util.UUID
import kotlin.collections.firstOrNull
import kotlin.collections.map

/**
 * Composite implementation that delegates to multiple test steps
 */
class CompositeTestStep internal constructor(private val steps: List<TestStep>) : TestStep, KoinComponent {

    override val id: String = UUID.randomUUID().toString()
    override val name: String = steps.firstOrNull()?.name ?: ""
    override val stepStaus: TestStatus = steps.firstOrNull()?.stepStaus ?: TestStatus.SKIPPED

    override fun updateStatus(status: TestStatus, newName: String?): TestStep {
        steps.forEach { it.updateStatus(status, newName) }
        return this
    }

    override fun addParameter(name: String, value: String): TestStep {
        steps.forEach { it.addParameter(name, value) }
        return this
    }

    override fun attachFile(name: String, content: ByteArray, type: AttachmentType): TestStep {
        steps.forEach { it.attachFile(name, content, type) }
        return this
    }

    override fun createChildStep(name: String): TestStep {
        val factory: CompositeTestStepFactory by inject()
        val childSteps = steps.map { it.createChildStep(name) }
        return factory.create(childSteps)
    }

    override fun complete() {
        steps.forEach { it.complete() }
    }

    /**
     * Executes a block of code as a child step
     */
    fun childStep(name: String, action: () -> Unit) {
        val childStep = createChildStep(name)
        try {
            action()
            childStep.pass()
        } catch (e: AssertionError) {
            childStep.fail()
            throw e
        } catch (e: Exception) {
            childStep.markBroken()
            throw e
        }
    }

    /**
     * Executes a block of code as a child step with boolean result
     */
    fun childStep(name: String, action: () -> Boolean) {
        val childStep = createChildStep(name)
        try {
            val result = action()
            if (result) childStep.pass() else childStep.fail()
        } catch (e: AssertionError) {
            childStep.fail()
            throw e
        } catch (e: Exception) {
            childStep.markBroken()
            throw e
        }
    }
}