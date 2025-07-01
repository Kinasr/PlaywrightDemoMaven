package io.github.kinasr.playwright_demo_maven.utils.report2.model

/**
 * Represents the possible statuses of a test or test step in the reporting system.
 */

enum class ReportStatus {
    PASSED, FAILED, BROKEN, SKIPPED
}

enum class AttachmentType(val type: String, val fileExtension: String) {
    TEXT("text/plain", ".txt"), JSON("application/json", ".json"),
    IMAGE("image/jpeg", ".jpg");
}

enum class LinkType {
    ISSUE, TMS, CUSTOM
}