package io.github.kinasr.playwright_demo_maven.di

import com.microsoft.playwright.Playwright
import io.github.kinasr.playwright_demo_maven.config.Config
import io.github.kinasr.playwright_demo_maven.config.ConfigLoader
import io.github.kinasr.playwright_demo_maven.config.ConfigRecord
import io.github.kinasr.playwright_demo_maven.playwright_manager.PlaywrightManager
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.browser.BrowserManager
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.screenshot.PlayScreenshot
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.screenshot.ScreenshotManager
import io.github.kinasr.playwright_demo_maven.utils.ScreenshotHelper
import io.github.kinasr.playwright_demo_maven.utils.TestDataProvider
import io.github.kinasr.playwright_demo_maven.utils.logger.LoggerName
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import io.github.kinasr.playwright_demo_maven.utils.report.Report
import io.qameta.allure.Allure
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mainModule = module {

    // Config
    single { ConfigLoader() }
    single<ConfigRecord>(named("config")) { get<ConfigLoader>().config }
    single { Config.Browser() }
    single { Config.App() }
    single { Config.Test() }

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

    factory<BrowserManager> { BrowserManager(get<Playwright>()) }
//    factory<Page> { (contextOptions: Browser.NewContextOptions) ->
//        get<BrowserManager>().getContext(contextOptions).newPage()
//    }

    single<ScreenshotManager> { 
        PlayScreenshot(get<PlayLogger>(named(LoggerName.PLAYWRIGHT)), "/target/screenshots") 
    }
}

var logModule = module {
    single(named(LoggerName.REPORT)) { PlayLogger.get(LoggerName.REPORT) }
    single(named(LoggerName.PLAYWRIGHT)) { PlayLogger.get(LoggerName.PLAYWRIGHT) }
}

val reportModule = module {
    single { Allure.getLifecycle() }

    single { Report() }
}