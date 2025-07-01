package io.github.kinasr.playwright_demo_maven.utils.report.core

import io.github.kinasr.playwright_demo_maven.utils.logger.LoggerName
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import io.github.kinasr.playwright_demo_maven.utils.report.model.AttachmentType
import io.github.kinasr.playwright_demo_maven.utils.report.model.ReportStatus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.util.UUID

/**
 * Base class for all report steps in the test execution.
 *
 * This abstract class provides the foundation for creating and managing test steps in the reporting system.
 * It handles step lifecycle, status updates, and attachments.
 *
 */
abstract class ReportStepCore protected constructor() : KoinComponent {
    protected val logger: PlayLogger by inject(named(LoggerName.REPORT))
    protected val uuid: String = UUID.randomUUID().toString()
    protected val startTime: Long = System.currentTimeMillis()
    protected var name = ""
    protected var endTime: Long? = null
    protected var stepStatus: ReportStatus = ReportStatus.SKIPPED
    
    
    abstract fun initialize(name: String): ReportStepCore
    /**
     * Updates the step's status and optionally its name.
     *
     * @param status The new status to set for the step
     * @param name Optional new name for the step. If null, the existing name is preserved.
     * @return The updated step instance for method chaining
     */
    abstract fun update(status: ReportStatus, name: String? = null): ReportStepCore
    
    /**
     * Adds a parameter to the step.
     *
     * @param name The name of the parameter
     * @param value The value of the parameter
     * @return The updated step instance for method chaining
     * @throws IllegalArgumentException if name is blank
     */
    abstract fun addParameter(name: String, value: String): ReportStepCore

    /**
     * Attaches content to the step.
     *
     * @param name The name of the attachment
     * @param content The binary content to attach
     * @param contentType The MIME type of the content (defaults to TEXT)
     * @throws IllegalArgumentException if name is blank or content is empty
     */
    abstract fun attach(name: String, content: ByteArray, contentType: AttachmentType = AttachmentType.TEXT)
    
    /**
     * Adds a child step with the given name.
     *
     * @param name The name of the child step
     * @return The newly created child step
     */
    abstract fun addChildStep(name: String): ReportStepCore
    
    /**
     * Adds a child step with the given name and status.
     *
     * @param name The name of the child step
     * @param status The status of the child step
     */
    abstract fun addChildStep(name: String, status: ReportStatus)
    
    
    /**
     * Closes the step, finalizing its state in the report.
     * This should be called when the step is complete.
     */
    abstract fun close()
}