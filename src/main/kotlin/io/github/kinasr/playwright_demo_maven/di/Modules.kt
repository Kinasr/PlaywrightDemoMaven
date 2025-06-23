package io.github.kinasr.playwright_demo_maven.di

import io.github.kinasr.playwright_demo_maven.browser.BrowserManager
import io.github.kinasr.playwright_demo_maven.config.Config
import io.github.kinasr.playwright_demo_maven.config.ConfigLoader
import io.github.kinasr.playwright_demo_maven.config.ConfigRecord
import io.github.kinasr.playwright_demo_maven.pages.HomePage
import io.github.kinasr.playwright_demo_maven.pages.LoginPage
import io.github.kinasr.playwright_demo_maven.utils.ScreenshotHelper
import io.github.kinasr.playwright_demo_maven.utils.TestDataProvider
import io.github.kinasr.playwright_demo_maven.utils.logger.LoggerName
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import io.github.kinasr.playwright_demo_maven.utils.report.allure.AllureReport
import io.github.kinasr.playwright_demo_maven.utils.report.allure.AllureReportStep
import io.github.kinasr.playwright_demo_maven.utils.report.core.ReportStep
import io.qameta.allure.Allure
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Main test module that includes all required dependencies for testing.
 */
val testModule = module {

    // Config
    single { ConfigLoader() }
    single<ConfigRecord>(named("config")) { get<ConfigLoader>().config }

    // Browser Management
    single { BrowserManager() }


    // Page Objects
    factory { HomePage(get<BrowserManager>().getPage()!!) }
    factory { LoginPage(get<BrowserManager>().getPage()!!) }

    // Utilities
    single { TestDataProvider() }
    single { ScreenshotHelper(get()) }

    // Logger
    single(named(LoggerName.REPORT)) { PlayLogger.get(LoggerName.REPORT) }
}

val reportModule = module {
    single { Allure.getLifecycle() }
    single { AllureReport(Config.Allure().resultsDirectory) }
    factory { AllureReportStep() }
}