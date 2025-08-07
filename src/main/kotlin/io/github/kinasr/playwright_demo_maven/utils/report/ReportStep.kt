package io.github.kinasr.playwright_demo_maven.utils.report

import io.github.kinasr.playwright_demo_maven.utils.report_old.model.AttachmentType
import io.qameta.allure.AllureLifecycle
import io.qameta.allure.model.Parameter
import io.qameta.allure.model.Status
import io.qameta.allure.model.StepResult
import jdk.jfr.internal.consumer.EventLog.update
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.Closeable

class ReportStep(private val uuid: String) : KoinComponent, Closeable {
    private val lifecycle by inject<AllureLifecycle>()

    fun start(msg: String): ReportStep {
        lifecycle.startStep(uuid, StepResult().setName(msg))
        return this
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


    override fun close() {
        TODO("Not yet implemented")
    }
}