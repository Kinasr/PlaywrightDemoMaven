package io.github.kinasr.playwright_demo_maven.utils.report_old.core

import io.github.kinasr.playwright_demo_maven.utils.report_old.model.AttachmentType
import io.github.kinasr.playwright_demo_maven.utils.report_old.model.TestStatus

/**
 * Interface for test steps
 */
interface TestStep {
    val id: String
    val name: String
    val stepStaus: TestStatus

    fun updateStatus(status: TestStatus, newName: String? = null): TestStep
    fun addParameter(name: String, value: String): TestStep
    fun attachFile(name: String, content: ByteArray, type: AttachmentType = AttachmentType.TEXT): TestStep
    fun createChildStep(name: String): TestStep
    fun complete()

    // Convenience methods
    fun pass(): TestStep = updateStatus(TestStatus.PASSED).also { complete() }
    fun fail(): TestStep = updateStatus(TestStatus.FAILED).also { complete() }
    fun skip(): TestStep = updateStatus(TestStatus.SKIPPED).also { complete() }
    fun markBroken(): TestStep = updateStatus(TestStatus.BROKEN).also { complete() }
}