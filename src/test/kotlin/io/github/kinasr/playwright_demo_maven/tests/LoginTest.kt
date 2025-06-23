package io.github.kinasr.playwright_demo_maven.tests

import io.github.kinasr.playwright_demo_maven.base.BaseTest
import io.github.kinasr.playwright_demo_maven.pages.HomePage
import io.github.kinasr.playwright_demo_maven.pages.LoginPage
import io.github.kinasr.playwright_demo_maven.utils.TestDataProvider
import io.qameta.allure.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.koin.test.inject

@Epic("Authentication")
@Feature("Login Functionality")
class LoginTest : BaseTest() {

    private val loginPage: LoginPage by inject()
    private val homePage: HomePage by inject()
    private val testDataProvider: TestDataProvider by inject()

    @BeforeEach
    override fun setUp(testInfo: TestInfo) {
        super.setUp(testInfo)
        navigateToApplication()
    }

    @Test
    @DisplayName("Successful login with valid credentials")
    @Description("Test successful login using valid username and password")
    @Story("User can login with valid credentials")
    @Severity(SeverityLevel.CRITICAL)
    fun testSuccessfulLogin() {
        // Given
        val user = TestDataProvider.VALID_USER

        // When
        loginPage.loginWith(user.username, user.password)

        // Then
        assertTrue(homePage.verifyHomePageIsDisplayed(), "Home page should be displayed after successful login")
        assertTrue(homePage.isUserLoggedIn(), "User should be logged in")

        val welcomeMessage = homePage.getWelcomeMessage()
        assertTrue(welcomeMessage.isNotEmpty(), "Welcome message should be displayed")

        screenshotHelper.takeScreenshotForStep("successful_login")
    }

    @Test
    @DisplayName("Login failure with invalid credentials")
    @Description("Test login failure using invalid username and password")
    @Story("User cannot login with invalid credentials")
    @Severity(SeverityLevel.CRITICAL)
    fun testLoginFailureWithInvalidCredentials() {
        // Given
        val user = TestDataProvider.INVALID_USER

        // When
        loginPage.loginWith(user.username, user.password)

        // Then
        assertTrue(loginPage.verifyLoginPageIsDisplayed(), "Login page should still be displayed after failed login")
        assertTrue(loginPage.isErrorMessageVisible(), "Error message should be visible")

        val errorMessage = loginPage.getErrorMessage()
        assertTrue(errorMessage.isNotEmpty(), "Error message should not be empty")

        screenshotHelper.takeScreenshotForStep("login_failure")
    }

    @Test
    @DisplayName("Login failure with empty credentials")
    @Description("Test login failure using empty username and password")
    @Story("User cannot login with empty credentials")
    @Severity(SeverityLevel.NORMAL)
    fun testLoginFailureWithEmptyCredentials() {
        // Given
        val user = TestDataProvider.EMPTY_USER

        // When
        loginPage.loginWith(user.username, user.password)

        // Then
        assertTrue(loginPage.verifyLoginPageIsDisplayed(), "Login page should still be displayed")
        assertFalse(loginPage.isLoginButtonEnabled(), "Login button should be disabled with empty credentials")

        screenshotHelper.takeScreenshotForStep("empty_credentials")
    }

    @ParameterizedTest
    @MethodSource("getValidUsers")
    @DisplayName("Login with multiple valid users")
    @Description("Test login functionality with different valid user accounts")
    @Story("Multiple users can login successfully")
    @Severity(SeverityLevel.NORMAL)
    fun testLoginWithMultipleUsers(user: TestDataProvider.User) {
        // When
        loginPage.loginWith(user.username, user.password)

        // Then
        assertTrue(homePage.verifyHomePageIsDisplayed(), "Home page should be displayed for user: ${user.username}")
        assertTrue(homePage.isUserLoggedIn(), "User ${user.username} should be logged in")

        // Logout for next iteration
        homePage.clickUserMenu().clickLogout()

        screenshotHelper.takeScreenshotForStep("login_user_${user.username}")
    }

    @Test
    @DisplayName("Remember me functionality")
    @Description("Test remember me checkbox functionality")
    @Story("User can use remember me option")
    @Severity(SeverityLevel.MINOR)
    fun testRememberMeFunctionality() {
        // Given
        val user = TestDataProvider.VALID_USER

        // When
        loginPage.enterUsername(user.username)
            .enterPassword(user.password)
            .checkRememberMe()
            .clickLoginButton()

        // Then
        assertTrue(homePage.verifyHomePageIsDisplayed(), "Home page should be displayed")

        screenshotHelper.takeScreenshotForStep("remember_me")
    }

    @Test
    @DisplayName("Forgot password link functionality")
    @Description("Test forgot password link navigation")
    @Story("User can access forgot password page")
    @Severity(SeverityLevel.MINOR)
    fun testForgotPasswordLink() {
        // When
        loginPage.clickForgotPassword()

        // Then
        val currentUrl = loginPage.getCurrentUrl()
        assertTrue(currentUrl.contains("forgot-password") || currentUrl.contains("reset"),
            "Should navigate to forgot password page")

        screenshotHelper.takeScreenshotForStep("forgot_password")
    }

    companion object {
        @JvmStatic
        fun getValidUsers(): List<TestDataProvider.User> {
            return TestDataProvider().getValidUsers()
        }
    }

    @AfterEach
    override fun tearDown(testInfo: TestInfo) {
        try {
            // Additional cleanup if needed
            if (homePage.isUserLoggedIn()) {
                homePage.clickUserMenu().clickLogout()
            }
        } catch (e: Exception) {
            logger.warn("Could not logout user during cleanup: ${e.message}")
        } finally {
            super.tearDown(testInfo)
        }
    }
}