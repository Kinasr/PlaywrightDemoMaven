package io.github.kinasr.playwright_demo_maven.utils.report.allure

import io.github.kinasr.playwright_demo_maven.config.Config
import io.github.kinasr.playwright_demo_maven.utils.file.PropertiesManager
import io.github.kinasr.playwright_demo_maven.utils.logger.LoggerName
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import io.github.kinasr.playwright_demo_maven.utils.report.core.TestReporter
import io.github.kinasr.playwright_demo_maven.utils.report.core.TestStep
import io.github.kinasr.playwright_demo_maven.utils.report.model.AttachmentType
import io.github.kinasr.playwright_demo_maven.utils.report.model.LinkType
import io.github.kinasr.playwright_demo_maven.utils.report.model.TestStatus
import io.github.kinasr.playwright_demo_maven.utils.requireNotBlank
import io.qameta.allure.Allure
import io.qameta.allure.AllureLifecycle
import io.qameta.allure.model.Link
import io.qameta.allure.model.Parameter
import io.qameta.allure.model.Status
import io.qameta.allure.model.StatusDetails
import io.qameta.allure.model.TestResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.inject
import java.io.File
import java.util.UUID

/**
 * Allure implementation of TestReporter
 */
class AllureTestReporter(private val reportPath: String) : TestReporter, KoinComponent {

    private val lifecycle: AllureLifecycle by inject()
    private val logger: PlayLogger by inject(named(LoggerName.REPORT))

    override fun initReporter(props:Map<String, String>) {
        PropertiesManager.createOrUpdateProperties(reportPath, "environment.properties", props)
    }
    
    override fun startTest(testName: String, description: String?) {
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

    override fun endTest(testName: String, status: TestStatus) {
        try {
            lifecycle.updateTestCase { testResult ->
                testResult.status = status.toAllureStatus()
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

    override fun createStep(name: String): TestStep {
        val step: AllureTestStep by inject()
        return step.apply { initialize(name) }
    }

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

    override fun attachFile(name: String, content: ByteArray, type: AttachmentType) {
        requireNotBlank(name, "Attachment name")
        require(content.isNotEmpty()) { "Attachment content cannot be empty" }

        try {
            Allure.getLifecycle().addAttachment(
                name,
                type.mimeType,
                type.extension,
                content.inputStream()
            )
            logger.trace { "Added attachment: $name (${content.size} bytes)" }
        } catch (e: Exception) {
            logger.error(e) { "Failed to add attachment: $name" }
            throw e
        }
    }

    override fun addLink(name: String, url: String, type: LinkType) {
        requireNotBlank(name, "Link name")
        requireNotBlank(url, "Link URL")
        require(url.matches(Regex("^https?://.+"))) { "Invalid URL format: $url" }

        try {
            lifecycle.updateTestCase { testResult ->
                testResult.links.add(
                    Link()
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

    override fun generateReport() {
        logger.info { "Allure report generation is handled by the Allure CLI. Use 'allure serve' to view the report." }
    }

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