package io.github.kinasr.playwright_demo_maven.utils.report_old.allure

import io.github.kinasr.playwright_demo_maven.utils.logger.LoggerName
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import io.github.kinasr.playwright_demo_maven.utils.report_old.CompositeTestStepFactory
import io.github.kinasr.playwright_demo_maven.utils.report_old.core.TestStep
import io.github.kinasr.playwright_demo_maven.utils.report_old.model.AttachmentType
import io.github.kinasr.playwright_demo_maven.utils.report_old.model.TestStatus
import io.github.kinasr.playwright_demo_maven.utils.requireNotBlank
import io.qameta.allure.Allure
import io.qameta.allure.AllureLifecycle
import io.qameta.allure.model.Parameter
import io.qameta.allure.model.StepResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.util.UUID

/**
 * Allure implementation of TestStep
 */
class AllureTestStep : TestStep, KoinComponent {

    private val lifecycle: AllureLifecycle by inject()
    private val logger: PlayLogger by inject(named(LoggerName.REPORT))
    private val stepFactory: CompositeTestStepFactory by inject()

    override val id: String = UUID.randomUUID().toString()
    override var name: String = ""
        private set
    override var stepStaus: TestStatus = TestStatus.SKIPPED
        private set

    private val startTime: Long = System.currentTimeMillis()
    private var endTime: Long? = null
    private var isCompleted: Boolean = false

    fun initialize(stepName: String): AllureTestStep {
        this.name = stepName
        lifecycle.startStep(id, StepResult().apply {
            setName(stepName)
            setStatus(stepStaus.toAllureStatus())
            setStart(startTime)
        })
        return this
    }

    override fun updateStatus(status: TestStatus, newName: String?): TestStep {
        checkNotCompleted()

        return try {
            lifecycle.updateStep(id) { stepResult ->
                stepResult.status = status.toAllureStatus()
                newName?.let {
                    stepResult.name = it
                    this.name = it
                }
                this.stepStaus = status
            }
            logger.trace { "Updated step '$name' with status: $status" }
            this
        } catch (e: Exception) {
            logger.error(e) { "Failed to update step '$name'" }
            throw e
        }
    }

    override fun addParameter(name: String, value: String): TestStep {
        requireNotBlank(name, "Parameter name")
        checkNotCompleted()

        return try {
            lifecycle.updateStep(id) { stepResult ->
                stepResult.parameters.add(
                    Parameter()
                        .setName(name)
                        .setValue(value)
                )
            }
            logger.trace { "Added parameter '$name=$value' to step '$name'" }
            this
        } catch (e: Exception) {
            logger.error(e) { "Failed to add parameter '$name' to step '$name'" }
            throw e
        }
    }

    override fun attachFile(name: String, content: ByteArray, type: AttachmentType): TestStep {
        requireNotBlank(name, "Attachment name")
        require(content.isNotEmpty()) { "Attachment content cannot be empty" }
        checkNotCompleted()

        try {
            Allure.getLifecycle().addAttachment(
                name,
                type.mimeType,
                type.extension,
                content.inputStream()
            )
            logger.trace { "Added attachment '$name' to step '${this.name}' (${content.size} bytes)" }
            return this
        } catch (e: Exception) {
            logger.error(e) { "Failed to add attachment '$name' to step '${this.name}'" }
            throw e
        }
    }

    override fun createChildStep(name: String): TestStep {
        requireNotBlank(name, "Step name")
        checkNotCompleted()

        return try {
            logger.trace { "Created child step '$name' for step '${this.name}'" }
            val childStep: AllureTestStep by inject()
            childStep.initialize(name)
        } catch (e: Exception) {
            logger.error(e) { "Failed to create child step '$name' for step '${this.name}'" }
            throw e
        }
    }

    override fun complete() {
        if (isCompleted) return

        endTime = System.currentTimeMillis()
        isCompleted = true

        lifecycle.updateStep(id) { stepResult ->
            stepResult.setStop(endTime!!)
        }
        lifecycle.stopStep(id)
    }

    private fun checkNotCompleted() {
        if (isCompleted) {
            throw IllegalStateException("Step '$name' is already completed")
        }
    }
}