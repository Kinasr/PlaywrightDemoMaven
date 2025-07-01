package io.github.kinasr.playwright_demo_maven.utils.report.allure

import io.github.kinasr.playwright_demo_maven.utils.report.allure.AllureHelper.mapToAllureStatus
import io.github.kinasr.playwright_demo_maven.utils.report.core.ReportStepCore
import io.github.kinasr.playwright_demo_maven.utils.report.model.AttachmentType
import io.github.kinasr.playwright_demo_maven.utils.report.model.ReportStatus
import io.github.kinasr.playwright_demo_maven.utils.requireNotBlank
import io.qameta.allure.Allure
import io.qameta.allure.AllureLifecycle
import io.qameta.allure.model.Parameter
import io.qameta.allure.model.Status
import io.qameta.allure.model.StepResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.ByteArrayInputStream

/**
 * Implementation of [ReportStepCore] for Allure reporting.
 *
 * This class represents a single step in an Allure test report and provides methods
 * to update the step's status, add parameters, and attach files.
 *
 */
class AllureReportStep() : ReportStepCore(), KoinComponent {
    private val lifecycle: AllureLifecycle by inject()
    
    override fun initialize(name: String): ReportStepCore {
        this.name = name
        lifecycle.startStep(this.uuid, uuid, StepResult().apply {
            setName(name)
            setStatus(mapToAllureStatus(stepStatus))
            setStart(startTime)
        })
        return this
    }

    /**
     * Updates the step's status and optionally its name.
     *
     * @param status The new status to set for the step
     * @param name Optional new name for the step. If null, the existing name is preserved.
     * @return The updated step instance for method chaining
     * @throws IllegalStateException if the step is already closed
     */
    override fun update(status: ReportStatus, name: String?): ReportStepCore {
        checkNotClosed()

        return try {
            lifecycle.updateStep(uuid) { stepResult ->
                stepResult.status = mapToAllureStatus(status)
                name?.let { stepResult.name = it }
                this.stepStatus = status
                this.name = name ?: this.name
            }
            logger.trace { "Updated step '${this.name}' with status: $status" }
            this
        } catch (e: Exception) {
            logger.error(e) { "Failed to update step '${this.name}'" }
            throw e
        }
    }

    /**
     * Adds a parameter to the step.
     *
     * @param name The name of the parameter
     * @param value The value of the parameter
     * @return The updated step instance for method chaining
     * @throws IllegalArgumentException if name is blank
     * @throws IllegalStateException if the step is already closed
     */
    override fun addParameter(name: String, value: String): ReportStepCore {
        requireNotBlank(name, "Parameter name")
        checkNotClosed()

        return try {
            lifecycle.updateStep(this.uuid) { stepResult ->
                stepResult.parameters.add(
                    Parameter()
                        .setName(name)
                        .setValue(value)
                )
            }
            logger.trace { "Added parameter '$name=$value' to step '${this.name}'" }
            this
        } catch (e: Exception) {
            logger.error(e) { "Failed to add parameter '$name' to step '${this.name}'" }
            throw e
        }
    }

    /**
     * Attaches content to the step.
     *
     * @param name The name of the attachment
     * @param content The binary content to attach
     * @param contentType The MIME type of the content
     * @throws IllegalArgumentException if name is blank or content is empty
     * @throws IllegalStateException if the step is already closed
     */
    override fun attach(
        name: String,
        content: ByteArray,
        contentType: AttachmentType
    ) {
        requireNotBlank(name, "Attachment name")
        require(content.isNotEmpty()) { "Attachment content cannot be empty" }
        checkNotClosed()

        try {
            Allure.getLifecycle().addAttachment(
                name,
                contentType.type,
                contentType.fileExtension,
                ByteArrayInputStream(content)
            )
            logger.trace { "Added attachment '$name' to step '${this.name}' (${content.size} bytes)" }
        } catch (e: Exception) {
            logger.error(e) { "Failed to add attachment '$name' to step '${this.name}'" }
            throw e
        }
    }

    /**
     * Adds a child step to this step.
     *
     * @param name The name of the child step
     * @return The newly created child step
     * @throws IllegalArgumentException if name is blank
     * @throws IllegalStateException if the step is already closed
     */
    override fun addChildStep(name: String): ReportStepCore {
        val childStep : AllureReportStep by inject()
        requireNotBlank(name, "Step name")
        checkNotClosed()

        return try {
            logger.trace { "Added child step '$name' to step '${this.name}'" }
            childStep.initialize(name)
        } catch (e: Exception) {
            logger.error(e) { "Failed to add child step '$name' to step '${this.name}'" }
            throw e
        }
    }

    override fun addChildStep(
        name: String,
        status: ReportStatus
    ) {
        val childStep : AllureReportStep by inject()
        requireNotBlank(name, "Step name")
        checkNotClosed()

        logger.trace { "Added child step '$name' to step '${this.name}' with status: $status" }
        childStep.initialize(name)
            .update(status)
            .close()
    }

    override fun close() {
        update(this.uuid) { result ->
            result.setStop(System.currentTimeMillis())
        }
        lifecycle.stopStep(this.uuid)
    }

    private fun update(id: String, result: (StepResult) -> StepResult) {
        lifecycle.updateStep(id) {
            result(it)
        }
    }

    /**
     * Checks if the step is already closed and throws an [IllegalStateException] if it is.
     *
     * @throws IllegalStateException if the step is already closed
     */
    private fun checkNotClosed() {
        if (endTime != null) {
            throw IllegalStateException("Step '${name}' is already closed")
        }
    }
}
