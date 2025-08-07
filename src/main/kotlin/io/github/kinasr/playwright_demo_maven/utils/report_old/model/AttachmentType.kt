package io.github.kinasr.playwright_demo_maven.utils.report_old.model

/**
 * File attachment types with proper MIME types
 */
enum class AttachmentType(val mimeType: String, val extension: String) {
    TEXT("text/plain", ".txt"),
    JSON("application/json", ".json"),
    XML("application/xml", ".xml"),
    HTML("text/html", ".html"),
    IMAGE_JPEG("image/jpeg", ".jpg"),
    IMAGE_PNG("image/png", ".png"),
    VIDEO_MP4("video/mp4", ".mp4"),
    PDF("application/pdf", ".pdf")
}