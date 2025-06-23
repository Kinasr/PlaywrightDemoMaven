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

@Epic("Home Page")
@Feature("Home Page Functionality")
class HomeTest : BaseTest() {

    private val loginPage: LoginPage by inject()
    private val homePage: HomePage by inject()
    private val testDataProvider: TestDataProvider by inject()

    @BeforeEach
    override fun setUp(testInfo: TestInfo) {
        super.setUp(testInfo)
        navigateToApplication()

        // Login before each test
        val user = TestDataProvider.VALID_USER
        loginPage.loginWith(user.username, user.password)
        assertTrue(homePage.verifyHomePageIsDisplayed(), "Should be logged in before running home page tests")
    }

    @Test
    @DisplayName("Verify home page elements are displayed")
    @Description("Test that all home page elements are properly displayed")
    @Story("User can see all home page elements")
    @Severity(SeverityLevel.CRITICAL)
    fun testHomePageElementsDisplay() {
        // Then
        assertTrue(homePage.verifyHomePageIsDisplayed(), "Home page should be displayed")
        assertTrue(homePage.isUserLoggedIn(), "User should be logged in")

        val welcomeMessage = homePage.getWelcomeMessage()
        assertTrue(welcomeMessage.isNotEmpty(), "Welcome message should be displayed")

        screenshotHelper.takeScreenshotForStep("home_page_elements")
    }

    @Test
    @DisplayName("User menu functionality")
    @Description("Test user menu interaction and options")
    @Story("User can interact with user menu")
    @Severity(SeverityLevel.NORMAL)
    fun testUserMenuFunctionality() {
        // When
        homePage.clickUserMenu()

        // Then
        val currentUserName = homePage.getCurrentUserName()
        assertTrue(currentUserName.isNotEmpty(), "Current user name should be displayed in menu")

        screenshotHelper.takeScreenshotForStep("user_menu")
    }

    @ParameterizedTest
    @MethodSource("getSearchQueries")
    @DisplayName("Search functionality with different queries")
    @Description("Test search functionality with various search terms")
    @Story("User can search for different items")
    @Severity(SeverityLevel.NORMAL)
    fun testSearchFunctionality(query: String) {
        // When
        homePage.search(query)

        // Then
        val currentUrl = homePage.getCurrentUrl()
        assertTrue(currentUrl.contains("search") || currentUrl.contains("results"),
            "Should navigate to search results page for query: $query")

        screenshotHelper.takeScreenshotForStep("search_$query")

        // Navigate back to home for next iteration
        navigateToApplication()
    }

    @Test
    @DisplayName("Navigation to profile page")
    @Description("Test navigation to user profile page")
    @Story("User can navigate to profile page")
    @Severity(SeverityLevel.NORMAL)
    fun testProfileNavigation() {
        // When
        homePage.navigateToProfile()

        // Then
        val currentUrl = homePage.getCurrentUrl()
        assertTrue(currentUrl.contains("profile"), "Should navigate to profile page")

        screenshotHelper.takeScreenshotForStep("profile_navigation")
    }

    @Test
    @DisplayName("Navigation to settings page")
    @Description("Test navigation to settings page")
    @Story("User can navigate to settings page")
    @Severity(SeverityLevel.NORMAL)
    fun testSettingsNavigation() {
        // When
        homePage.navigateToSettings()

        // Then
        val currentUrl = homePage.getCurrentUrl()
        assertTrue(currentUrl.contains("settings"), "Should navigate to settings page")

        screenshotHelper.takeScreenshotForStep("settings_navigation")
    }

    @Test
    @DisplayName("Logout functionality")
    @Description("Test user logout functionality")
    @Story("User can logout successfully")
    @Severity(SeverityLevel.CRITICAL)
    fun testLogoutFunctionality() {
        // When
        homePage.clickUserMenu().clickLogout()

        // Then
        assertTrue(loginPage.verifyLoginPageIsDisplayed(), "Should be redirected to login page after logout")
        assertFalse(homePage.isUserLoggedIn(), "User should not be logged in after logout")

        screenshotHelper.takeScreenshotForStep("logout")
    }

    @Test
    @DisplayName("Page title verification")
    @Description("Test that home page has correct title")
    @Story("Home page displays correct title")
    @Severity(SeverityLevel.MINOR)
    fun testPageTitle() {
        // When
        val pageTitle = homePage.getPageTitle()

        // Then
        assertTrue(pageTitle.isNotEmpty(), "Page title should not be empty")
        assertTrue(pageTitle.contains("Home") || pageTitle.contains("Dashboard"),
            "Page title should indicate home/dashboard page")

        screenshotHelper.takeScreenshotForStep("page_title")
    }

    @Test
    @DisplayName("Welcome message personalization")
    @Description("Test that welcome message is personalized for the user")
    @Story("Welcome message shows user information")
    @Severity(SeverityLevel.MINOR)
    fun testWelcomeMessagePersonalization() {
        // When
        val welcomeMessage = homePage.getWelcomeMessage()
        val currentUser = homePage.getCurrentUserName()

        // Then
        assertTrue(welcomeMessage.isNotEmpty(), "Welcome message should not be empty")
        if (currentUser.isNotEmpty()) {
            assertTrue(welcomeMessage.contains(currentUser, ignoreCase = true),
                "Welcome message should contain user name")
        }

        screenshotHelper.takeScreenshotForStep("welcome_personalization")
    }

    companion object {
        @JvmStatic
        fun getSearchQueries(): List<String> {
            return TestDataProvider().getSearchQueries()
        }
    }

    @AfterEach
    override fun tearDown(testInfo: TestInfo) {
        try {
            // Ensure logout for clean state
            if (homePage.isUserLoggedIn()) {
                homePage.clickUserMenu().clickLogout()
            }
        } catch (e: Exception) {
            logger.warn("Could not logout during cleanup: ${e.message}")
        } finally {
            super.tearDown(testInfo)
        }
    }
}