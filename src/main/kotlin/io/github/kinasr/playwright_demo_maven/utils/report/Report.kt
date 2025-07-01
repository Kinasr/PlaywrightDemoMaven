package io.github.kinasr.playwright_demo_maven.utils.report

import io.github.kinasr.playwright_demo_maven.utils.report.manager.ReportFactory
import io.github.kinasr.playwright_demo_maven.utils.report.model.AttachmentType
import io.github.kinasr.playwright_demo_maven.utils.report.model.LinkType
import io.github.kinasr.playwright_demo_maven.utils.report.model.ReportStatus
import org.koin.core.component.KoinComponent

object Report : KoinComponent {

    fun start() {
        ReportFactory.reports().forEach {
            it.start("Playwright Demo Maven", "Playwright Demo Maven")
        }
    }

    fun end(status: ReportStatus) {
        ReportFactory.reports().forEach {
            it.end("Playwright Demo Maven", status)
        }
    }

    fun addStep(name: String, status: ReportStatus) {
        ReportFactory.reports().forEach {
            it.addStep(name, status)
        }
    }

    fun addStep(name: String): ReportStep {
        val steps = ReportFactory.reports().map {
            it.addStep(name)
        }.toList()

        return ReportStep(steps)
    }

    fun addStep(name: String, block: () -> Unit) {
        val steps = ReportFactory.reports().map {
            it.addStep(name)
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

    fun addStep(name: String, block: () -> Boolean) {
        val steps = ReportFactory.reports().map {
            it.addStep(name)
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

    fun addParameter(name: String, value: String) {
        ReportFactory.reports().forEach {
            it.addParameter(name, value)
        }
    }

    fun attach(name: String, content: ByteArray, contentType: AttachmentType = AttachmentType.TEXT) {
        ReportFactory.reports().forEach {
            it.attach(name, content, contentType)
        }
    }

    fun addLink(name: String, url: String, type: LinkType = LinkType.CUSTOM) {
        ReportFactory.reports().forEach {
            it.addLink(name, url, type)
        }
    }

    fun generate() {
        ReportFactory.reports().forEach {
            it.generate()
        }
    }

    fun cleanup() {
        ReportFactory.reports().forEach {
            it.cleanup()
        }
    }
}
