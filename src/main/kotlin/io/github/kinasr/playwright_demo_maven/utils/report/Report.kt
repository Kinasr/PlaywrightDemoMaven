package io.github.kinasr.playwright_demo_maven.utils.report

import io.github.kinasr.playwright_demo_maven.utils.report.model.AttachmentType
import io.github.kinasr.playwright_demo_maven.utils.report.model.LinkType
import io.qameta.allure.Allure
import io.qameta.allure.AllureLifecycle
import io.qameta.allure.model.Link
import io.qameta.allure.model.Parameter
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Report : KoinComponent {
    private val lifecycle by inject<AllureLifecycle>()

    fun epic(name: String) {
        Allure.epic(name)
    }

    fun parameter(name: String, value: String) {
        lifecycle.updateTestCase {
            it.parameters.add(
                Parameter()
                    .setName(name)
                    .setValue(value)
            )
        }
    }

    fun link(name: String, url: String, type: LinkType = LinkType.CUSTOM) {
        lifecycle.updateTestCase { testResult ->
            testResult.links.add(
                Link()
                    .setName(name)
                    .setUrl(url)
                    .setType(type.name.lowercase())
            )
        }
    }

    fun attach(name: String, content: ByteArray, type: AttachmentType = AttachmentType.TEXT) {
        lifecycle.addAttachment(
            name,
            type.mimeType,
            type.extension,
            content.inputStream()
        )
    }

    fun step(name: String): ReportStep {
        return ReportStep.start(lifecycle, name)
    }
}