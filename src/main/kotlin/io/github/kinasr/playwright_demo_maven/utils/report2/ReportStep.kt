package io.github.kinasr.playwright_demo_maven.utils.report2

import io.github.kinasr.playwright_demo_maven.utils.report2.core.ReportStepCore
import io.github.kinasr.playwright_demo_maven.utils.report2.model.AttachmentType
import io.github.kinasr.playwright_demo_maven.utils.report2.model.ReportStatus

class ReportStep constructor(private val reportsStep: List<ReportStepCore>) {
    
    fun update(status: ReportStatus, name: String? = null): ReportStep {
        reportsStep.forEach { it.update(status, name) }
        return this
    }
    
    fun addParameter(name: String, value: String): ReportStep {
        reportsStep.forEach { it.addParameter(name, value) }
        return this
    }
    
    fun attach(name: String, content: ByteArray, contentType: AttachmentType = AttachmentType.TEXT): ReportStep { 
        reportsStep.forEach { it.attach(name, content, contentType) }
        return this
    }
    
    fun addChildStep(name: String): ReportStep {
        val childReportsStep = reportsStep.map { it.addChildStep(name) }
        return ReportStep(childReportsStep)
    }
    
    fun addChildStep(name: String, status: ReportStatus): ReportStep {
        reportsStep.forEach { it.addChildStep(name, status) }
        return this
    }

    fun addChildStep(name: String, block: () -> Unit) {
        val steps = reportsStep.map {
            it.addChildStep(name)
        }.toList()
        try {
            block()
            steps.forEach { it.update(ReportStatus.PASSED).close() }
        } catch (e: AssertionError) {
            steps.forEach { it.update(ReportStatus.FAILED).close() }
            throw e
        } catch (e: Exception) {
            steps.forEach { it.update(ReportStatus.BROKEN).close() }
            throw e
        }
    }

    fun addChildStep(name: String, block: () -> Boolean) {
        val steps = reportsStep.map {
            it.addChildStep(name)
        }.toList()
        try {
            val result = block()
            steps.forEach { it.update(if (result) ReportStatus.PASSED else ReportStatus.FAILED).close() }
        } catch (e: AssertionError) {
            steps.forEach { it.update(ReportStatus.FAILED).close() }
            throw e
        } catch (e: Exception) {
            steps.forEach { it.update(ReportStatus.BROKEN).close() }
            throw e
        }
    }
    
    fun passed(): ReportStep {
        reportsStep.forEach { it.update(ReportStatus.PASSED).close() }
        return this
    }
    
    fun failed(): ReportStep {
        reportsStep.forEach { it.update(ReportStatus.FAILED).close() }
        return this
    }
    
    fun broken(): ReportStep {
        reportsStep.forEach { it.update(ReportStatus.BROKEN).close() }
        return this
    }
    
    fun skipped(): ReportStep {
        reportsStep.forEach { it.update(ReportStatus.SKIPPED).close() }
        return this
    }
    
    fun close(): ReportStep {
        reportsStep.forEach { it.close() }
        return this
    }
}