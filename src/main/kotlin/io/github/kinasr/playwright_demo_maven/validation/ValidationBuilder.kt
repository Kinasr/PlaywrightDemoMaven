package io.github.kinasr.playwright_demo_maven.validation

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page
import io.github.kinasr.playwright_demo_maven.playwright_manager.api.model.APIResult
import io.github.kinasr.playwright_demo_maven.playwright_manager.api.validation.APIValidation
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.GUI
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action.element.GUIElementValidation
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.action.page.GUIPageValidation
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.model.GUIElement
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.model.GUIElementI
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import io.github.kinasr.playwright_demo_maven.utils.report.Report
import org.opentest4j.AssertionFailedError

open class ValidationBuilder(
    val logger: PlayLogger,
    val report: Report,
    val performer: ValidationPerformer
) {
    private val validations: MutableList<() -> Throwable?> = mutableListOf()

    fun validate(gui: GUI, element: GUIElementI): GUIElementValidation {
        return GUIElementValidation(this, gui, element)
    }

    fun validate(gui: GUI, locator: Locator): GUIElementValidation {
        return GUIElementValidation(this, gui, GUIElement(locator))
    }

    fun validate(page: Page): GUIPageValidation {
        return GUIPageValidation(this, page)
    }

    fun <T> validate(apiResult: APIResult<T>): APIValidation<T> {
        return APIValidation(this, apiResult)
    }

    fun validate(str: String): StringValidation {
        return StringValidation(this, str)
    }

    fun assert() {
        val step = report.step("Assert all validations")
        validations.forEach { validation ->
            validation()?.let { throwable ->
                logger.error { "Assertion failed" }
                step.failed()
                throw throwable
            }
        }

        logger.info { "All assertions passed" }
        step.passed()
    }

    fun verify() {
        val step = report.step("Verify all validations")
        val failures = mutableListOf<Throwable>()

        validations.forEach { validation ->
            validation()?.let {
                failures.add(it)
            }
        }

        if (failures.isNotEmpty()) {
            val errorMessage = "Verification failed with ${failures.size} error(s)"

            logger.error { errorMessage }
            step.failed("Verification failed")
            throw AssertionFailedError(errorMessage)
        } else {
            logger.info { "All verifications passed" }
            step.passed()
        }
    }

    internal fun addValidation(validation: () -> Throwable?) {
        validations.add(validation)
    }
}