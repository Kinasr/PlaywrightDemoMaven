package io.github.kinasr.playwright_demo_maven.di

import com.microsoft.playwright.*
import io.github.kinasr.playwright_demo_maven.config.Config
import io.github.kinasr.playwright_demo_maven.config.ConfigLoader
import io.github.kinasr.playwright_demo_maven.config.ConfigRecord
import io.github.kinasr.playwright_demo_maven.pages.ABTestingPage
import io.github.kinasr.playwright_demo_maven.pages.WelcomePage
import io.github.kinasr.playwright_demo_maven.playwright_manager.PlaywrightManager
import io.github.kinasr.playwright_demo_maven.playwright_manager.api.manager.APIRequestManager
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.GUI
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.manager.BrowserContextManager
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.manager.BrowserManager
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.screenshot.PlayScreenshot
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.screenshot.ScreenshotManager
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.validation.ValidationBuilder
import io.github.kinasr.playwright_demo_maven.utils.logger.LoggerName
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import io.github.kinasr.playwright_demo_maven.utils.report.Report
import io.qameta.allure.Allure
import org.koin.core.qualifier.named
import org.koin.dsl.module

// Define the test scope qualifier
val configModule = module {
    single<ConfigLoader> { ConfigLoader() }
    single<ConfigRecord> { get<ConfigLoader>().config }
    single { Config.Playwright(get<ConfigRecord>().playwright) }
    single { Config.Browser(get<ConfigRecord>().browser) }
    single { Config.App(get<ConfigRecord>().app) }
    single { Config.Test(get<ConfigRecord>().test) }
    single { Config.Allure(get<ConfigRecord>().allure) }
    single { Config.Logging(get<ConfigRecord>().logging) }
}

var logModule = module {
    single(named(LoggerName.REPORT)) {
        PlayLogger.get(
            LoggerName.REPORT,
            get<Config.Logging>()
        )
    }
    single(named(LoggerName.PLAYWRIGHT)) {
        PlayLogger.get(
            LoggerName.PLAYWRIGHT,
            get<Config.Logging>()
        )
    }

    single(named(LoggerName.VALIDATION)) {
        PlayLogger.get(
            LoggerName.VALIDATION,
            get<Config.Logging>()
        )
    }
}

val reportModule = module {
    single { Allure.getLifecycle() }
    single { Report() }
}

val playwrightModule = module {
    single { PlaywrightManager(get<PlayLogger>(named(LoggerName.PLAYWRIGHT))) }

    single<Playwright> {
        get<PlaywrightManager>().initialize {
            this.env = get<Config.Playwright>().env
        }
    }

    factory<BrowserManager> {
        BrowserManager(
            logger = get<PlayLogger>(named(LoggerName.PLAYWRIGHT)),
            browserConfig = get(),
            playwright = get()
        )
    }

    single<ScreenshotManager> {
        PlayScreenshot(get<PlayLogger>(named(LoggerName.PLAYWRIGHT)), "/screenshots")
    }

    factory { (options: Browser.NewContextOptions) ->
        BrowserContextManager(
            logger = get<PlayLogger>(named(LoggerName.PLAYWRIGHT)),
            browser = get<BrowserManager>().browser(),
            contextOptions = options
        )
    }
    
    factory<APIRequestManager> { params ->
        val context = params.getOrNull<APIRequest.NewContextOptions>()
        
        APIRequestManager(
            logger = get<PlayLogger>(named(LoggerName.PLAYWRIGHT)),
            playwright = get(),
            appConfig = get(),
            contextOptions = context
        )
    }

    scope(named(PlaywrightTestScope.TEST_SCOPE)) {
        scoped<BrowserContextManager> { get() }
        scoped<BrowserContext> { get() }
        scoped<Page> { get() }

        scoped<ValidationBuilder> {
            ValidationBuilder(
                logger = get<PlayLogger>(named(LoggerName.VALIDATION)),
                report = get<Report>(),
                screenshot = get<ScreenshotManager>(),
                context = get()
            )
        }

        scoped<GUI> {
            GUI(
                logger = get(named(LoggerName.PLAYWRIGHT)),
                report = get(),
                screenshot = get(),
                context = get(),
                validationBuilder = get()
            )
        }
    }

}

val pagesModule = module {
    scope(named(PlaywrightTestScope.TEST_SCOPE)) {
        scoped { WelcomePage(get(), get()) }
        scoped { ABTestingPage(get(), get()) }
    }
}

val mainModule = module {
    includes(configModule, playwrightModule, logModule, reportModule, pagesModule)
}
