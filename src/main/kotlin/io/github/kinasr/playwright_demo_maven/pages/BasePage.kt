package io.github.kinasr.playwright_demo_maven.pages

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page
import com.microsoft.playwright.options.WaitForSelectorState
import io.github.kinasr.playwright_demo_maven.config.Config
import io.qameta.allure.kotlin.Attachment
import io.qameta.allure.kotlin.Step
import org.slf4j.LoggerFactory

abstract class BasePage(protected val page: Page) {
    protected val logger = LoggerFactory.getLogger(this::class.java)

    @Step("Navigate to {url}")
    open fun navigateTo(url: String) {
        logger.info("Navigating to: $url")
        page.navigate(url)
    }

    @Step("Navigate to base URL")
    open fun navigateToBaseUrl() {
        navigateTo(Config.App().baseUrl)
    }

    @Step("Wait for element to be visible: {selector}")
    protected fun waitForVisible(selector: String, timeout: Double = Config.Browser().timeout): Locator {
        logger.debug("Waiting for element to be visible: $selector")
        return page.locator(selector).waitFor(
            Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(timeout)
        ).let { page.locator(selector) }
    }

    @Step("Wait for element to be hidden: {selector}")
    protected fun waitForHidden(selector: String, timeout: Double = Config.Browser().timeout): Locator {
        logger.debug("Waiting for element to be hidden: $selector")
        return page.locator(selector).waitFor(
            Locator.WaitForOptions()
                .setState(WaitForSelectorState.HIDDEN)
                .setTimeout(timeout)
        ).let { page.locator(selector) }
    }

    @Step("Click element: {selector}")
    protected fun click(selector: String) {
        logger.debug("Clicking element: $selector")
        waitForVisible(selector).click()
    }

    @Step("Fill text '{text}' in element: {selector}")
    protected fun fill(selector: String, text: String) {
        logger.debug("Filling text '$text' in element: $selector")
        waitForVisible(selector).fill(text)
    }

    @Step("Get text from element: {selector}")
    protected fun getText(selector: String): String {
        logger.debug("Getting text from element: $selector")
        return waitForVisible(selector).textContent() ?: ""
    }

    @Step("Check if element is visible: {selector}")
    protected fun isVisible(selector: String): Boolean {
        logger.debug("Checking if element is visible: $selector")
        return page.locator(selector).isVisible
    }

    @Step("Check if element is enabled: {selector}")
    protected fun isEnabled(selector: String): Boolean {
        logger.debug("Checking if element is enabled: $selector")
        return page.locator(selector).isEnabled
    }

    @Step("Wait for page load")
    protected fun waitForPageLoad() {
        logger.debug("Waiting for page load")
        page.waitForLoadState()
    }

    @Step("Get page title")
    fun getPageTitle(): String {
        logger.debug("Getting page title")
        return page.title()
    }

    @Step("Get current URL")
    fun getCurrentUrl(): String {
        logger.debug("Getting current URL")
        return page.url()
    }

    @Step("Scroll to element: {selector}")
    protected fun scrollToElement(selector: String) {
        logger.debug("Scrolling to element: $selector")
        page.locator(selector).scrollIntoViewIfNeeded()
    }

    @Step("Select option '{option}' from dropdown: {selector}")
    protected fun selectOption(selector: String, option: String) {
        logger.debug("Selecting option '$option' from dropdown: $selector")
        page.locator(selector).selectOption(option)
    }

    @Step("Press key: {key}")
    protected fun pressKey(key: String) {
        logger.debug("Pressing key: $key")
        page.keyboard().press(key)
    }
}