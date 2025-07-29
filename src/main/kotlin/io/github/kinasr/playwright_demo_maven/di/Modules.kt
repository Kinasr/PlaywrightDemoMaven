package io.github.kinasr.playwright_demo_maven.di

import com.microsoft.playwright.Browser
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import io.github.kinasr.playwright_demo_maven.config.Config
import io.github.kinasr.playwright_demo_maven.config.ConfigLoader
import io.github.kinasr.playwright_demo_maven.config.ConfigRecord
import io.github.kinasr.playwright_demo_maven.playwright_manager.PlaywrightManager
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.browser.BrowserManager
import io.github.kinasr.playwright_demo_maven.utils.ScreenshotHelper
import io.github.kinasr.playwright_demo_maven.utils.TestDataProvider
import io.github.kinasr.playwright_demo_maven.utils.logger.LoggerName
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import io.github.kinasr.playwright_demo_maven.utils.report.CompositeTestStepFactory
import io.github.kinasr.playwright_demo_maven.utils.report.CompositeTestStepFactoryImpl
import io.github.kinasr.playwright_demo_maven.utils.report.Report
import io.github.kinasr.playwright_demo_maven.utils.report.allure.AllureTestReporter
import io.github.kinasr.playwright_demo_maven.utils.report.allure.AllureTestStep
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mainModule = module {

    // Config
    single { ConfigLoader() }
    single<ConfigRecord>(named("config")) { get<ConfigLoader>().config }

    // Browser Management
//    single { BrowserManager() }


    // Page Objects
//    factory { HomePage(get<BrowserManager>().getPage()!!) }
//    factory { LoginPage(get<BrowserManager>().getPage()!!) }

    // Utilities
    single { TestDataProvider() }
    single { ScreenshotHelper(get()) }
}

val playwrightModule = module {
    single<Playwright> { PlaywrightManager().initialize() }
    factory<Page> { (contextOptions: Browser.NewContextOptions) ->
        BrowserManager(get<Playwright>()).getContext(contextOptions).newPage()
    }
}

var logModule = module {
    single(named(LoggerName.REPORT)) { PlayLogger.get(LoggerName.REPORT) }
    single(named(LoggerName.PLAYWRIGHT)) { PlayLogger.get(LoggerName.PLAYWRIGHT) }
}

val reportModule = module {
    // Factories
    single<CompositeTestStepFactory> { CompositeTestStepFactoryImpl() }

    // Reporters
    single { AllureTestReporter(Config.Allure().resultsDirectory) }

    // Steps - Use factory pattern for steps since they need to be created per test
    factory { AllureTestStep() }

    single { Report() }
}