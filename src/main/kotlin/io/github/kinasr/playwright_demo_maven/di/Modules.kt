package io.github.kinasr.playwright_demo_maven.di

import com.microsoft.playwright.Playwright
import io.github.kinasr.playwright_demo_maven.config.Config
import io.github.kinasr.playwright_demo_maven.config.ConfigLoader
import io.github.kinasr.playwright_demo_maven.config.ConfigRecord
import io.github.kinasr.playwright_demo_maven.playwright_manager.PlaywrightManager
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.manager.BrowserManager
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
}

val configModule = module {
    single<ConfigRecord> { get<ConfigLoader>().config }
    single { Config.Playwright(get<ConfigRecord>().playwright) }
    single { Config.Browser(get<ConfigRecord>().browser) }
    single { Config.App(get<ConfigRecord>().app) }
    single { Config.Test(get<ConfigRecord>().test) }
    single { Config.Allure(get<ConfigRecord>().allure) }
    single { Config.Logging(get<ConfigRecord>().logging) }
}

val playwrightModule = module {
    single<Playwright> { PlaywrightManager().initialize{
        this.env = get<Config.Playwright>().env
    } }

    factory<BrowserManager> { BrowserManager(get<Playwright>()) }

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