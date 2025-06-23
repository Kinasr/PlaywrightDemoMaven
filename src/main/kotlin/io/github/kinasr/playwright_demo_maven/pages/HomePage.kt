package io.github.kinasr.playwright_demo_maven.pages

import com.microsoft.playwright.Page
import io.qameta.allure.kotlin.Step

class HomePage(page: Page) : BasePage(page) {

    // Locators
    private val welcomeMessage = "#welcome-message"
    private val userMenu = "#user-menu"
    private val logoutButton = "#logout"
    private val searchBox = "#search"
    private val searchButton = "#search-button"
    private val navigationMenu = ".nav-menu"
    private val profileLink = "#profile-link"
    private val settingsLink = "#settings-link"

    @Step("Get welcome message")
    fun getWelcomeMessage(): String {
        logger.info("Getting welcome message")
        return getText(welcomeMessage)
    }

    @Step("Click user menu")
    fun clickUserMenu(): HomePage {
        logger.info("Clicking user menu")
        click(userMenu)
        return this
    }

    @Step("Click logout button")
    fun clickLogout(): HomePage {
        logger.info("Clicking logout button")
        click(logoutButton)
        waitForPageLoad()
        return this
    }

    @Step("Search for: {query}")
    fun search(query: String): HomePage {
        logger.info("Searching for: $query")
        fill(searchBox, query)
        click(searchButton)
        waitForPageLoad()
        return this
    }

    @Step("Navigate to profile")
    fun navigateToProfile(): HomePage {
        logger.info("Navigating to profile")
        click(profileLink)
        waitForPageLoad()
        return this
    }

    @Step("Navigate to settings")
    fun navigateToSettings(): HomePage {
        logger.info("Navigating to settings")
        click(settingsLink)
        waitForPageLoad()
        return this
    }

    @Step("Verify home page is displayed")
    fun verifyHomePageIsDisplayed(): Boolean {
        logger.info("Verifying home page is displayed")
        return isVisible(welcomeMessage) &&
                isVisible(userMenu) &&
                isVisible(navigationMenu)
    }

    @Step("Check if user is logged in")
    fun isUserLoggedIn(): Boolean {
        logger.info("Checking if user is logged in")
        return isVisible(userMenu) && isVisible(welcomeMessage)
    }

    @Step("Get current user name")
    fun getCurrentUserName(): String {
        logger.info("Getting current user name")
        clickUserMenu()
        return getText("#current-user-name")
    }
}