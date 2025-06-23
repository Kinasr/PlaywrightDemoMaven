package io.github.kinasr.playwright_demo_maven.utils

import org.slf4j.LoggerFactory

class TestDataProvider {
    private val logger = LoggerFactory.getLogger(TestDataProvider::class.java)

    data class User(
        val username: String,
        val password: String,
        val email: String,
        val firstName: String,
        val lastName: String
    )

    companion object {
        // Valid test users
        val VALID_USER = User(
            username = "testuser",
            password = "TestPass123!",
            email = "testuser@example.com",
            firstName = "Test",
            lastName = "User"
        )

        val ADMIN_USER = User(
            username = "admin",
            password = "AdminPass123!",
            email = "admin@example.com",
            firstName = "Admin",
            lastName = "User"
        )

        // Invalid test users
        val INVALID_USER = User(
            username = "invaliduser",
            password = "wrongpassword",
            email = "invalid@example.com",
            firstName = "Invalid",
            lastName = "User"
        )

        val EMPTY_USER = User(
            username = "",
            password = "",
            email = "",
            firstName = "",
            lastName = ""
        )
    }

    fun getValidUsers(): List<User> {
        logger.info("Providing valid test users")
        return listOf(VALID_USER, ADMIN_USER)
    }

    fun getInvalidUsers(): List<User> {
        logger.info("Providing invalid test users")
        return listOf(INVALID_USER, EMPTY_USER)
    }

    fun getSearchQueries(): List<String> {
        logger.info("Providing search queries")
        return listOf(
            "automation",
            "testing",
            "playwright",
            "kotlin",
            "framework"
        )
    }

    fun getTestUrls(): Map<String, String> {
        logger.info("Providing test URLs")
        return mapOf(
            "login" to "/login",
            "home" to "/home",
            "profile" to "/profile",
            "settings" to "/settings",
            "dashboard" to "/dashboard"
        )
    }

    fun generateRandomEmail(): String {
        val randomString = (1..8).map { ('a'..'z').random() }.joinToString("")
        return "test$randomString@example.com"
    }

    fun generateRandomUsername(): String {
        val randomString = (1..8).map { ('a'..'z').random() }.joinToString("")
        return "user$randomString"
    }

    fun generateRandomPassword(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%"
        return (1..12).map { chars.random() }.joinToString("")
    }
}