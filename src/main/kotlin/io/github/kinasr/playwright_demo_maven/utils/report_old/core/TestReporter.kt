package io.github.kinasr.playwright_demo_maven.utils.report_old.core

import io.github.kinasr.playwright_demo_maven.utils.report_old.model.AttachmentType
import io.github.kinasr.playwright_demo_maven.utils.report_old.model.LinkType
import io.github.kinasr.playwright_demo_maven.utils.report_old.model.TestStatus

/**
 * Core interface for test reporting systems
 */
interface TestReporter {
    fun initReporter(props:Map<String, String>)
    fun startTest(testName: String, description: String? = null)
    fun endTest(testName: String, status: TestStatus)
    fun createStep(name: String): TestStep
    fun addParameter(name: String, value: String)
    fun attachFile(name: String, content: ByteArray, type: AttachmentType = AttachmentType.TEXT)
    fun addLink(name: String, url: String, type: LinkType = LinkType.CUSTOM)
    fun generateReport()
    fun cleanup()
}