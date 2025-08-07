package io.github.kinasr.playwright_demo_maven.di

import com.microsoft.playwright.Browser
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import io.github.kinasr.playwright_demo_maven.config.ConfigLoader
import io.github.kinasr.playwright_demo_maven.config.ConfigRecord
import io.github.kinasr.playwright_demo_maven.playwright_manager.PlaywrightManager
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.browser.BrowserManager
import io.github.kinasr.playwright_demo_maven.utils.ScreenshotHelper
import io.github.kinasr.playwright_demo_maven.utils.TestDataProvider
import io.github.kinasr.playwright_demo_maven.utils.logger.LoggerName
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import io.qameta.allure.Allure
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
    single { Allure.getLifecycle() }
}