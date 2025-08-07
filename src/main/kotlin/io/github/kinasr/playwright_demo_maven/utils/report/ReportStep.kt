package io.github.kinasr.playwright_demo_maven.utils.report

import io.github.kinasr.playwright_demo_maven.utils.report.model.AttachmentType
import io.qameta.allure.AllureLifecycle
import io.qameta.allure.model.Parameter
import io.qameta.allure.model.Status
import io.qameta.allure.model.StepResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.Closeable
import java.util.*

class ReportStep(private val uuid: String) : KoinComponent, Closeable {
    private val lifecycle by inject<AllureLifecycle>()

    companion object {
        fun start(lifecycle: AllureLifecycle, name: String, parentUUID: String? = null): ReportStep {
            val uuid = UUID.randomUUID().toString()

            if (parentUUID != null) {
                lifecycle.startStep(parentUUID, uuid, StepResult().setName(name))
            } else {
                lifecycle.startStep(uuid, StepResult().setName(name))
            }

            return ReportStep(uuid)
        }
    }

    fun parameter(name: String, value: String): ReportStep {
        return update {
            it.parameters.add(
                Parameter()
                    .setName(name)
                    .setValue(value)
            )
        }
    }

    fun attach(name: String, content: ByteArray, type: AttachmentType = AttachmentType.TEXT): ReportStep {
        lifecycle.addAttachment(
            name,
            type.mimeType,
            type.extension,
            content.inputStream()
        )
        return this
    }

    fun updateStatus(status: Status, newName: String? = null): ReportStep {
        return update {
            if (newName != null) it.name = newName
            it.status = status
        }
    }

    fun update(result: (StepResult) -> Unit): ReportStep {
        lifecycle.updateStep(uuid, result)
        return this
    }

    fun passed(newName: String? = null) {
        updateStatus(Status.PASSED, newName)
        close()
    }

    fun failed(newName: String? = null) {
        updateStatus(Status.FAILED, newName)
        close()
    }

    fun skipped(newName: String? = null) {
        updateStatus(Status.SKIPPED, newName)
        close()
    }

    fun broken(newName: String? = null) {
        updateStatus(Status.BROKEN, newName)
        close()
    }

    fun step(name: String): ReportStep {
        return start(lifecycle, name, uuid)
    }


    override fun close() {
        lifecycle.stopStep(uuid)
    }
}