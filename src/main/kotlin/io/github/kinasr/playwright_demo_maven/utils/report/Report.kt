package io.github.kinasr.playwright_demo_maven.utils.report

import io.github.kinasr.playwright_demo_maven.utils.report.core.TestStep
import io.github.kinasr.playwright_demo_maven.utils.report.factory.ReporterFactory
import io.github.kinasr.playwright_demo_maven.utils.report.model.AttachmentType
import io.github.kinasr.playwright_demo_maven.utils.report.model.LinkType
import io.github.kinasr.playwright_demo_maven.utils.report.model.TestStatus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Main facade for the reporting system
 * Provides a clean API for test reporting operations
 */
object Report: KoinComponent {
    private val reporters get() = ReporterFactory.getActiveReporters()
    private val stepFactory: CompositeTestStepFactory by inject()

    fun init(props: Map<String, String>) {
        reporters.forEach { it.initReporter(props) }
    }
    
    /**
     * Starts a new test case
     */
    fun startTest(testName: String, description: String? = null) {
        reporters.forEach { it.startTest(testName, description) }
    }

    /**
     * Ends the current test case
     */
    fun endTest(testName: String, status: TestStatus) {
        reporters.forEach { it.endTest(testName, status) }
    }

    /**
     * Creates a new test step
     */
    fun step(name: String): TestStep {
        val steps = reporters.map { it.createStep(name) }
        return stepFactory.create(steps)
    }

    /**
     * Executes a block of code as a test step
     */
    fun step(name: String, action: () -> Unit) {
        val step = step(name)
        try {
            action()
            step.pass()
        } catch (e: AssertionError) {
            step.fail()
            throw e
        } catch (e: Exception) {
            step.markBroken()
            throw e
        }
    }

    /**
     * Executes a block of code as a test step with boolean result
     */
    fun step(name: String, action: () -> Boolean) {
        val step = step(name)
        try {
            val result = action()
            if (result) step.pass() else step.fail()
        } catch (e: AssertionError) {
            step.fail()
            throw e
        } catch (e: Exception) {
            step.markBroken()
            throw e
        }
    }

    /**
     * Adds a parameter to the current test
     */
    fun addParameter(name: String, value: String) {
        reporters.forEach { it.addParameter(name, value) }
    }

    /**
     * Attaches a file to the current test
     */
    fun attachFile(name: String, content: ByteArray, type: AttachmentType = AttachmentType.TEXT) {
        reporters.forEach { it.attachFile(name, content, type) }
    }

    /**
     * Adds a link to the current test
     */
    fun addLink(name: String, url: String, type: LinkType = LinkType.CUSTOM) {
        reporters.forEach { it.addLink(name, url, type) }
    }

    /**
     * Generates all reports
     */
    fun generate() {
        reporters.forEach { it.generateReport() }
    }

    /**
     * Cleans up all reporters
     */
    fun cleanup() {
        reporters.forEach { it.cleanup() }
    }
}