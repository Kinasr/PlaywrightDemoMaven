package io.github.kinasr.playwright_demo_maven.utils.report.allure

import io.github.kinasr.playwright_demo_maven.config.Config
import io.github.kinasr.playwright_demo_maven.utils.report.allure.AllureHelper.mapToAllureStatus
import io.github.kinasr.playwright_demo_maven.utils.report.core.ReportCore
import io.github.kinasr.playwright_demo_maven.utils.report.core.ReportStepCore
import io.github.kinasr.playwright_demo_maven.utils.report.model.AttachmentType
import io.github.kinasr.playwright_demo_maven.utils.report.model.LinkType
import io.github.kinasr.playwright_demo_maven.utils.report.model.ReportStatus
import io.github.kinasr.playwright_demo_maven.utils.requireNotBlank
import io.qameta.allure.Allure
import io.qameta.allure.AllureLifecycle
import io.qameta.allure.model.Parameter
import io.qameta.allure.model.Status
import io.qameta.allure.model.StatusDetails
import io.qameta.allure.model.TestResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.util.*

/**
 * Implementation of the [ReportCore] interface for Allure reporting.
 *
 * This class handles the creation and management of Allure test reports, including test steps,
 * attachments, and test metadata.
 */
class AllureReport(reportPath: String) : ReportCore(reportPath), KoinComponent {

    private val lifecycle: AllureLifecycle by inject()

    /**
     * Starts a new test case in the Allure report.
     *
     * @param testName The name of the test case
     * @param description Optional description of the test case
     * @throws IllegalArgumentException if testName is blank
     */
    override fun start(testName: String, description: String?) {
        requireNotBlank(testName, "Test name")

        val testResult = TestResult().apply {
            uuid = UUID.randomUUID().toString()
            name = testName
            description?.let { setDescription(it) }
            start = System.currentTimeMillis()
            status = Status.PASSED
        }

        try {
            lifecycle.scheduleTestCase(testResult)
            lifecycle.startTestCase(testResult.uuid)
            logger.debug { "Started test case: $testName" }
        } catch (e: Exception) {
            logger.error(e) { "Failed to start test case: $testName" }
            throw e
        }
    }

    /**
     * Ends the current test case with the given status.
     *
     * @param testName The name of the test case to end
     * @param status The final status of the test case
     * @throws IllegalStateException if no test case is currently running
     */
    override fun end(testName: String, status: ReportStatus) {
        try {
            lifecycle.updateTestCase { testResult ->
                testResult.status = mapToAllureStatus(status)
                testResult.statusDetails = StatusDetails()
                testResult.stop = System.currentTimeMillis()
            }

            lifecycle.stopTestCase(testName)
            lifecycle.writeTestCase(testName)
            logger.debug { "Ended test case: $testName with status: $status" }
        } catch (e: Exception) {
            logger.error(e) { "Failed to end test case: $testName" }
            throw e
        }
    }

    override fun addStep(name: String): ReportStepCore {
        val reportStep: AllureReportStep by inject()
        requireNotBlank(name, "Step name")

        try {
            logger.trace { "Add step: $name" }
            return reportStep.initialize(name)
        } catch (e: Exception) {
            logger.error(e) { "Failed to add completed step: $name" }
            throw e
        }
    }

    /**
     * Adds a step with the given name and status.
     *
     * @param name The name of the step
     * @param status The status of the step
     * @throws IllegalArgumentException if name is blank
     */
    override fun addStep(name: String, status: ReportStatus) {
        val reportStep: AllureReportStep by inject()
        requireNotBlank(name, "Step name")

        try {
            reportStep.initialize(name)
                .update(status)
                .close()
            logger.trace { "Added completed step: $name with status: $status" }
        } catch (e: Exception) {
            logger.error(e) { "Failed to add completed step: $name" }
            throw e
        }
    }

    /**
     * Adds a parameter to the current test case.
     *
     * @param name The name of the parameter
     * @param value The value of the parameter
     * @throws IllegalArgumentException if name is blank
     */
    override fun addParameter(name: String, value: String) {
        requireNotBlank(name, "Parameter name")

        try {
            lifecycle.updateTestCase { testResult ->
                testResult.parameters.add(
                    Parameter()
                        .setName(name)
                        .setValue(value)
                )
            }
            logger.trace { "Added parameter: $name=$value" }
        } catch (e: Exception) {
            logger.error(e) { "Failed to add parameter: $name" }
            throw e
        }
    }

    /**
     * Attaches content to the current test case.
     *
     * @param name The name of the attachment
     * @param content The binary content to attach
     * @param contentType The MIME type of the content
     * @throws IllegalArgumentException if name is blank or content is empty
     */
    override fun attach(
        name: String,
        content: ByteArray,
        contentType: AttachmentType
    ) {
        requireNotBlank(name, "Attachment name")
        require(content.isNotEmpty()) { "Attachment content cannot be empty" }

        try {
            Allure.getLifecycle().addAttachment(
                name,
                contentType.type,
                contentType.fileExtension,
                content.inputStream()
            )
            logger.trace { "Added attachment: $name (${content.size} bytes)" }
        } catch (e: Exception) {
            logger.error(e) { "Failed to add attachment: $name" }
            throw e
        }
    }

    /**
     * Adds a link to the current test case.
     *
     * @param name The display name of the link
     * @param url The URL to link to
     * @param type The type of link
     * @throws IllegalArgumentException if name or url is blank, or url is not valid
     */
    override fun addLink(
        name: String,
        url: String,
        type: LinkType
    ) {
        requireNotBlank(name, "Link name")
        requireNotBlank(url, "Link URL")
        require(url.matches(Regex("^https?://.+"))) { "Invalid URL format: $url" }

        try {
            lifecycle.updateTestCase { testResult ->
                testResult.links.add(
                    io.qameta.allure.model.Link()
                        .setName(name)
                        .setUrl(url)
                        .setType(type.name.lowercase())
                )
            }
            logger.trace { "Added link: $name ($url) of type: $type" }
        } catch (e: Exception) {
            logger.error(e) { "Failed to add link: $name ($url)" }
            throw e
        }
    }

    /**
     * Generates the Allure report.
     * This is a no-op in this implementation as Allure typically generates reports after test execution.
     */
    override fun generate() {
        logger.info { "Allure report generation is handled by the Allure CLI. Use 'allure serve' to view the report." }
    }

    /**
     * Cleans up the Allure results directory.
     * This will delete all existing test results.
     */
    override fun cleanup() {
        try {
            val resultsDir = File(Config.Allure().resultsDirectory)
            if (resultsDir.exists()) {
                resultsDir.deleteRecursively()
                resultsDir.mkdirs()
                logger.info { "Cleaned up Allure results directory: ${resultsDir.absolutePath}" }
            }
        } catch (e: Exception) {
            logger.error(e) { "Failed to clean up Allure results directory" }
            throw e
        }
    }
}