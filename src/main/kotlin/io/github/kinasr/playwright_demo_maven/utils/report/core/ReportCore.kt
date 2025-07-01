package io.github.kinasr.playwright_demo_maven.utils.report.core

import io.github.kinasr.playwright_demo_maven.utils.logger.LoggerName
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import io.github.kinasr.playwright_demo_maven.utils.report.model.AttachmentType
import io.github.kinasr.playwright_demo_maven.utils.report.model.LinkType
import io.github.kinasr.playwright_demo_maven.utils.report.model.ReportStatus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named


abstract class ReportCore(private val reportPath: String) : KoinComponent {
    protected val logger: PlayLogger by inject<PlayLogger>(named(LoggerName.REPORT))

    abstract fun start(testName: String, description: String? = null)
    
    abstract fun end(testName: String, status: ReportStatus)

    abstract fun addStep(name: String): ReportStepCore
    
    abstract fun addStep(name: String, status: ReportStatus)
    
    abstract fun addParameter(name: String, value: String)
    
    abstract fun attach(name: String, content: ByteArray, contentType: AttachmentType = AttachmentType.TEXT)
    
    abstract fun addLink(name: String, url: String, type: LinkType = LinkType.CUSTOM)
    
    abstract fun generate()
    
    abstract fun cleanup()
}