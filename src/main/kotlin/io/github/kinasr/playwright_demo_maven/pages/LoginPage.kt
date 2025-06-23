package io.github.kinasr.playwright_demo_maven.pages

import com.microsoft.playwright.Page
import io.qameta.allure.kotlin.Step

class LoginPage(page: Page) : BasePage(page) {

    // Locators
    private val usernameField = "#username"
    private val passwordField = "#password"
    private val loginButton = "#login-button"
    private val errorMessage = ".error-message"
    private val rememberMeCheckbox = "#remember-me"
    private val forgotPasswordLink = "#forgot-password"

    @Step("Enter username: {username}")
    fun enterUsername(username: String): LoginPage {
        logger.info("Entering username: $username")
        fill(usernameField, username)
        return this
    }

    @Step("Enter password")
    fun enterPassword(password: String): LoginPage {
        logger.info("Entering password")
        fill(passwordField, password)
        return this
    }

    @Step("Click login button")
    fun clickLoginButton(): LoginPage {
        logger.info("Clicking login button")
        click(loginButton)
        return this
    }

    @Step("Check remember me checkbox")
    fun checkRememberMe(): LoginPage {
        logger.info("Checking remember me checkbox")
        click(rememberMeCheckbox)
        return this
    }

    @Step("Click forgot password link")
    fun clickForgotPassword(): LoginPage {
        logger.info("Clicking forgot password link")
        click(forgotPasswordLink)
        return this
    }

    @Step("Login with credentials")
    fun loginWith(username: String, password: String): LoginPage {
        logger.info("Logging in with username: $username")
        enterUsername(username)
        enterPassword(password)
        clickLoginButton()
        waitForPageLoad()
        return this
    }

    @Step("Get error message")
    fun getErrorMessage(): String {
        logger.info("Getting error message")
        return getText(errorMessage)
    }

    @Step("Check if login button is enabled")
    fun isLoginButtonEnabled(): Boolean {
        return isEnabled(loginButton)
    }

    @Step("Check if error message is visible")
    fun isErrorMessageVisible(): Boolean {
        return isVisible(errorMessage)
    }

    @Step("Verify login page is displayed")
    fun verifyLoginPageIsDisplayed(): Boolean {
        logger.info("Verifying login page is displayed")
        return isVisible(usernameField) &&
                isVisible(passwordField) &&
                isVisible(loginButton)
    }
}