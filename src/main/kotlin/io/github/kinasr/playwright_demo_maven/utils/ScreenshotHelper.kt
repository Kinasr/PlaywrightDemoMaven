package io.github.kinasr.playwright_demo_maven.utils

import io.github.kinasr.playwright_demo_maven.browser.BrowserManager
import io.qameta.allure.kotlin.Allure
import io.qameta.allure.kotlin.Step
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ScreenshotHelper(private val browserManager: BrowserManager) {
    private val logger = LoggerFactory.getLogger(ScreenshotHelper::class.java)

    @Step("Take screenshot: {name}")
    fun takeScreenshot(name: String = "screenshot"): ByteArray? {
        return try {
            val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))
            val screenshotName = "${name}_$timestamp"

            logger.info("Taking screenshot: $screenshotName")
            val screenshot = browserManager.takeScreenshot(screenshotName)

            // Attach screenshot to Allure report
            screenshot?.let {
                Allure.attachment(screenshotName, it.inputStream(), "image/png", "png")
            }

            screenshot
        } catch (e: Exception) {
            logger.error("Failed to take screenshot: ${e.message}", e)
            null
        }
    }

    @Step("Take screenshot on failure")
    fun takeScreenshotOnFailure(testName: String): ByteArray? {
        return takeScreenshot("failure_$testName")
    }

    @Step("Take screenshot for step: {stepName}")
    fun takeScreenshotForStep(stepName: String): ByteArray? {
        return takeScreenshot("step_$stepName")
    }

    @Step("Attach text to Allure report")
    fun attachTextToAllure(name: String, content: String) {
        logger.info("Attaching text to Allure report: $name")
        Allure.attachment(name, content, "text/plain")
    }

    @Step("Attach JSON to Allure report")
    fun attachJsonToAllure(name: String, jsonContent: String) {
        logger.info("Attaching JSON to Allure report: $name")
        Allure.attachment(name, jsonContent, "application/json")
    }

    @Step("Attach HTML to Allure report")
    fun attachHtmlToAllure(name: String, htmlContent: String) {
        logger.info("Attaching HTML to Allure report: $name")
        Allure.attachment(name, htmlContent, "text/html")
    }
}