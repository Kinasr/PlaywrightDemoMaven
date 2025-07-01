package io.github.kinasr.playwright_demo_maven.utils.report2.allure

import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import io.github.kinasr.playwright_demo_maven.utils.report2.model.ReportStatus
import io.qameta.allure.model.Status
import io.qameta.allure.model.StatusDetails
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Utility object providing helper functions for Allure reporting integration.
 *
 * This object contains methods to convert between the framework's reporting model
 * and Allure's reporting model, as well as other Allure-specific utilities.
 */
object AllureHelper : KoinComponent {
    
    private val logger: PlayLogger by inject()
    
    /**
     * Maps a [ReportStatus] to an Allure [Status].
     *
     * @param status The status to map
     * @return The corresponding Allure status
     */
    fun mapToAllureStatus(status: ReportStatus): Status {
        return when (status) {
            ReportStatus.PASSED -> Status.PASSED
            ReportStatus.FAILED -> Status.FAILED
            ReportStatus.SKIPPED -> Status.SKIPPED
            ReportStatus.BROKEN -> Status.BROKEN
        }
    }
    
    /**
     * Creates a [StatusDetails] object with the given message and trace.
     *
     * @param message The error or status message
     * @param trace The stack trace (if any)
     * @return A new StatusDetails instance
     */
    fun createStatusDetails(message: String? = null, trace: String? = null): StatusDetails {
        return StatusDetails().apply {
            message?.let { this.message = it.take(1024) } // Limit message length
            trace?.let { this.trace = it }
        }
    }
    
    /**
     * Creates a [StatusDetails] object from a throwable.
     *
     * @param throwable The throwable to extract details from
     * @return A new StatusDetails instance with message and stack trace
     */
    fun createStatusDetails(throwable: Throwable): StatusDetails {
        return StatusDetails().apply {
            message = throwable.message?.take(1024) // Limit message length
            trace = throwable.stackTraceToString()
        }
    }
    
    /**
     * Converts an Allure [Status] to a [ReportStatus].
     *
     * @param status The Allure status to convert
     * @return The corresponding ReportStatus
     */
    fun mapFromAllureStatus(status: Status): ReportStatus {
        return when (status) {
            Status.PASSED -> ReportStatus.PASSED
            Status.FAILED -> ReportStatus.FAILED
            Status.BROKEN -> ReportStatus.BROKEN
            Status.SKIPPED -> ReportStatus.SKIPPED
        }
    }
}