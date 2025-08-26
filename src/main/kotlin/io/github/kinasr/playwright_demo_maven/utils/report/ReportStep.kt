package io.github.kinasr.playwright_demo_maven.utils.report

import io.github.kinasr.playwright_demo_maven.utils.logger.LoggerName
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import io.github.kinasr.playwright_demo_maven.utils.report.model.AttachmentType
import io.qameta.allure.AllureLifecycle
import io.qameta.allure.model.Parameter
import io.qameta.allure.model.Status
import io.qameta.allure.model.StepResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.Closeable
import java.util.*

class ReportStep private constructor(
    private val logger: PlayLogger,
    private val lifecycle: AllureLifecycle,
    private val name: String,
    private val uuid: String
) : KoinComponent, Closeable {
    private var isClosed = false

    companion object {
        fun start(
            logger: PlayLogger,
            lifecycle: AllureLifecycle,
            name: String,
            parentUUID: String? = null
        ): ReportStep {
            val uuid = UUID.randomUUID().toString()
            val stepResult = StepResult()
                .setName(name)
                .setStart(System.currentTimeMillis())

            runCatching {
                if (parentUUID != null) {
                    logger.trace { "Starting nested step '$name' under parent: $parentUUID" }
                    lifecycle.startStep(parentUUID, uuid, stepResult)
                } else {
                    logger.trace { "Starting root step: '$name'" }
                    lifecycle.startStep(uuid, stepResult)
                }
            }.onFailure {
                logger.warn { "Failed to start step '$name': ${it.message}" }
            }

            return ReportStep(logger, lifecycle, name, uuid)
        }
    }

    fun parameter(name: String, value: Any?): ReportStep {
        require(name.isNotBlank()) { "Parameter name cannot be blank" }
        checkNotClosed()

        return update {
            this.parameters.add(
                Parameter()
                    .setName(name)
                    .setValue(value?.toString() ?: "null")
            )
            logger.trace { "Parameter '$name: $value' added to step '${this.name}'" }
        }
    }

    fun attach(name: String, content: ByteArray, type: AttachmentType = AttachmentType.TEXT): ReportStep {
        checkNotClosed()

        runCatching {
            lifecycle.addAttachment(
                name,
                type.mimeType,
                type.extension,
                content.inputStream()
            )

        }.onSuccess {
            logger.trace { "Attachment '$name' added to step '${this.name}' successfully" }

        }.onFailure {
            logger.warn { "Failed to add attachment '$name' to step '${this.name}': ${it.message}" }
        }
        return this
    }

    fun step(name: String): ReportStep {
        checkNotClosed()
        return start(logger, lifecycle, name, uuid)
    }

    fun updateStatus(status: Status, newName: String? = null): ReportStep {
        return update {
            if (newName != null) this.name = newName
            this.status = status
        }
    }

    fun update(result: (StepResult.() -> Unit)): ReportStep {
        checkNotClosed()

        runCatching {
            lifecycle.updateStep(uuid) { it.result() }
        }.onFailure {
            logger.warn { "Failed to update step '${this.name}': ${it.message}" }
        }
        return this
    }

    fun passed(newName: String? = null): Unit = finishStep(Status.PASSED, newName)
    fun failed(newName: String? = null, details: String? = null): Unit = finishStep(Status.FAILED, newName, details)
    fun skipped(newName: String? = null, reason: String? = null): Unit = finishStep(Status.SKIPPED, newName, reason)
    fun broken(newName: String? = null, details: String? = null): Unit = finishStep(Status.BROKEN, newName, details)

    private fun checkNotClosed() {
        check(!isClosed) { "Step '$name' is already closed" }
    }

    private fun finishStep(status: Status, newName: String?, statusDetails: String? = null) {
        if (isClosed) {
            logger.warn { "Attempt to finish already closed step: '$name'" }
            return
        }

        update {
            if (newName != null) this.name = newName
            this.status = status
            this.stop = System.currentTimeMillis()
            if (statusDetails != null) {
                this.statusDetails = io.qameta.allure.model.StatusDetails()
                    .setMessage(statusDetails)
                logger.trace { "Added status details to step '${this.name}': $statusDetails" }
            }
        }
        close()
    }

    override fun close() {
        if (!isClosed) {
            runCatching {
                lifecycle.stopStep(uuid)
                logger.trace { "Step '$name' stopped successfully" }
            }.onFailure {
                logger.warn { "Failed to stop step '$name': ${it.message}" }
            }
            isClosed = true
        }
    }
}