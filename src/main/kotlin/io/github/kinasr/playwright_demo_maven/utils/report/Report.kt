package io.github.kinasr.playwright_demo_maven.utils.report

import io.github.kinasr.playwright_demo_maven.utils.logger.LoggerName
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import io.github.kinasr.playwright_demo_maven.utils.report.model.AttachmentType
import io.github.kinasr.playwright_demo_maven.utils.report.model.LinkType
import io.qameta.allure.Allure
import io.qameta.allure.AllureLifecycle
import io.qameta.allure.model.Link
import io.qameta.allure.model.Parameter
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import org.opentest4j.TestAbortedException

class Report : KoinComponent {
    private val logger by inject<PlayLogger>(named(LoggerName.REPORT))
    private val lifecycle by inject<AllureLifecycle>()

    fun epic(name: String): Report {
        runCatching {
            Allure.epic(name)
        }.onSuccess {
            logger.trace { "Epic '$name' set successfully" }
        }.onFailure {
            logger.warn { "Failed to set epic: '$name' : ${it.message}" }
        }

        return this
    }

    fun feature(name: String): Report {
        runCatching {
            Allure.feature(name)
        }.onSuccess {
            logger.trace { "Feature '$name' set successfully" }
        }.onFailure {
            logger.warn { "Failed to set feature '$name': ${it.message}" }
        }

        return this
    }

    fun story(name: String): Report {
        runCatching {
            Allure.story(name)
        }.onSuccess {
            logger.trace { "Story '$name' set successfully" }
        }.onFailure {
            logger.warn { "Failed to set story '$name': ${it.message}" }
        }
        return this
    }

    fun label(name: String, value: String): Report {
        runCatching {
            Allure.label(name, value)
        }.onSuccess {
            logger.trace { "Label '$name: $value' set successfully" }
        }.onFailure {
            logger.warn { "Failed to set label '$name: $value': ${it.message}" }
        }
        return this
    }

    fun parameter(name: String, value: Any?): Report {
        require(name.isNotBlank()) { "Parameter name cannot be blank" }

        runCatching {
            lifecycle.updateTestCase {
                it.parameters.add(
                    Parameter()
                        .setName(name)
                        .setValue(value?.toString() ?: "null")
                )
            }
        }.onSuccess {
            logger.trace { "Parameter '$name: $value' added successfully" }
        }.onFailure {
            logger.error { "Failed to add parameter '$name: $value': ${it.message}" }
        }

        return this
    }

    fun link(name: String, url: String, type: LinkType = LinkType.CUSTOM): Report {
        require(name.isNotBlank()) { "Link name cannot be blank" }

        runCatching {
            lifecycle.updateTestCase {
                it.links.add(
                    Link()
                        .setName(name)
                        .setUrl(url)
                        .setType(type.name.lowercase())
                )
            }
        }.onSuccess {
            logger.trace { "Link '$type $name: $url' added successfully" }
        }.onFailure {
            logger.error { "Failed to add link '$type $name: $url': ${it.message}" }
        }

        return this
    }

    fun attach(name: String, content: ByteArray, type: AttachmentType = AttachmentType.TEXT): Report {
        runCatching {
            lifecycle.addAttachment(
                name,
                type.mimeType,
                type.extension,
                content.inputStream()
            )
        }.onSuccess {
            logger.trace { "Attachment '$name' added successfully" }
        }.onFailure {
            logger.warn { "Failed to add attachment '$name': ${it.message}" }
        }
        return this
    }

    fun step(name: String): ReportStep {
        return ReportStep.start(lifecycle, logger, name)
    }

    inline fun <T> step(name: String, action: ReportStep.() -> T): T {
        require(name.isNotBlank()) { "Step name cannot be blank" }

        return step(name).use { step ->
            try {
                val result = step.action()
                step.passed()
                result
            } catch (e: TestAbortedException) {
                step.skipped(reason = e.message)
                throw e
            } catch (e: AssertionError) {
                step.failed(details = e.message)
                throw e
            } catch (e: Exception) {
                step.broken(details = e.message)
                throw e
            }
        }
    }
}