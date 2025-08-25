package io.github.kinasr.playwright_demo_maven.di

import com.google.gson.GsonBuilder
import com.microsoft.playwright.*
import io.github.kinasr.playwright_demo_maven.aut.api.TaskAPICollection
import io.github.kinasr.playwright_demo_maven.config.Config
import io.github.kinasr.playwright_demo_maven.config.ConfigLoader
import io.github.kinasr.playwright_demo_maven.config.ConfigRecord
import io.github.kinasr.playwright_demo_maven.pages.ABTestingPage
import io.github.kinasr.playwright_demo_maven.pages.WelcomePage
import io.github.kinasr.playwright_demo_maven.playwright_manager.PlaywrightManager
import io.github.kinasr.playwright_demo_maven.playwright_manager.api.action.APIAction
import io.github.kinasr.playwright_demo_maven.playwright_manager.api.manager.APIRequestManager
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.GUI
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.GUIPerformer
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.manager.BrowserContextManager
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.manager.BrowserManager
import io.github.kinasr.playwright_demo_maven.utils.constant.DateTimeFormatters
import io.github.kinasr.playwright_demo_maven.utils.gson_adapter.DoubleAdapter
import io.github.kinasr.playwright_demo_maven.utils.gson_adapter.ZonedDateTimeAdapter
import io.github.kinasr.playwright_demo_maven.utils.logger.LoggerName
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import io.github.kinasr.playwright_demo_maven.utils.report.Report
import io.github.kinasr.playwright_demo_maven.validation.GUIValidationBuilder
import io.github.kinasr.playwright_demo_maven.validation.ValidationBuilder
import io.github.kinasr.playwright_demo_maven.validation.ValidationPerformer
import io.qameta.allure.Allure
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.time.ZonedDateTime

// Define the test scope qualifier
val configModule = module {
    single<ConfigLoader> { ConfigLoader() }
    single<ConfigRecord> { get<ConfigLoader>().config }
    single { Config(get()) }
}

var logModule = module {
    single(named(LoggerName.REPORT)) {
        PlayLogger.get(
            LoggerName.REPORT,
            get()
        )
    }
    single(named(LoggerName.PLAYWRIGHT)) {
        PlayLogger.get(
            LoggerName.PLAYWRIGHT,
            get()
        )
    }

    single(named(LoggerName.VALIDATION)) {
        PlayLogger.get(
            LoggerName.VALIDATION,
            get()
        )
    }
}

val reportModule = module {
    single { Allure.getLifecycle() }
    single { Report() }
}

val utilsModule = module {
    single {
        GsonBuilder()
            .registerTypeAdapter(
                ZonedDateTime::class.java, ZonedDateTimeAdapter(
                    listOf(
                        DateTimeFormatters.ZONED_DATE_TIME_FORMATTER,
                        DateTimeFormatters.ZONED_DATE_TIME_FORMATTER_WITHOUT_DECIMAL
                    )
                )
            )
            .registerTypeAdapter(Double::class.java, DoubleAdapter())
            .create()
    }
}

val validationModel = module {
    single {
        ValidationPerformer(
            logger = get<PlayLogger>(named(LoggerName.VALIDATION)),
            report = get()
        )
    }

    single<ValidationBuilder> {
        ValidationBuilder(
            logger = get<PlayLogger>(named(LoggerName.VALIDATION)),
            report = get(),
            performer = get()
        )
    }

    factory<GUIValidationBuilder> {
        GUIValidationBuilder(
            logger = get<PlayLogger>(named(LoggerName.VALIDATION)),
            report = get(),
            performer = get()
        )
    }

//    scope(named(PlaywrightTestScope.TEST_SCOPE)) {
//        scoped<GUIValidationBuilder> {
//            GUIValidationBuilder(
//                logger = get<PlayLogger>(named(LoggerName.VALIDATION)),
//                report = get(),
//                performer = get(),
//                screenshotManager = get(),
//                context = get()
//            )
//        }
//    }
}

val playwrightModule = module {
    single { PlaywrightManager(get<PlayLogger>(named(LoggerName.PLAYWRIGHT))) }

    single<Playwright> {
        get<PlaywrightManager>().initialize {
            this.env = get<Config>().playwright.env
        }
    }
}

val guiModule = module {
    factory<BrowserManager> {
        BrowserManager(
            logger = get<PlayLogger>(named(LoggerName.PLAYWRIGHT)),
            config = get(),
            playwright = get()
        )
    }

//    factory<PageScreenshotManager> {
//        PageScreenshotManager(
//            logger = get<PlayLogger>(named(LoggerName.PLAYWRIGHT)),
//            page = get(),
//            directory = "./screenshots"
//        )
//    }

//    factory<LocatorScreenshotManager> { (locator: Locator) ->
//        LocatorScreenshotManager(
//            logger = get<PlayLogger>(named(LoggerName.PLAYWRIGHT)),
//            locator = locator,
//            directory = "./screenshots"
//        )
//    }

    factory<BrowserContextManager> { params ->
        val context = params.getOrNull<Browser.NewContextOptions>() ?: Browser.NewContextOptions()

        BrowserContextManager(
            logger = get<PlayLogger>(named(LoggerName.PLAYWRIGHT)),
            config = get(),
            browser = get<BrowserManager>().browser(),
            contextOptions = context
        )
    }

    factory<BrowserContext> { get<BrowserContextManager>().context() }

    factory<GUIPerformer> {
        GUIPerformer(
            logger = get<PlayLogger>(named(LoggerName.PLAYWRIGHT)),
            report = get(),
        )
    }

    factory<GUI> {
        GUI(
            performer = get(),
            validationBuilder = get(),
            context = get()
        )
    }

//    scope(named(PlaywrightTestScope.TEST_SCOPE)) {
////        scoped<BrowserContext> { get() }
////        scoped<Page> { get() }
//
//        scoped<GUI> {
//            GUI(
//                logger = get(named(LoggerName.PLAYWRIGHT)),
//                report = get(),
//                screenshot = get(),
//                context = get(),
//                validationBuilder = get()
//            )
//        }
//    }
}

val apiModule = module {
    factory<APIRequestManager> { params ->
        val context = params.getOrNull<APIRequest.NewContextOptions>() ?: APIRequest.NewContextOptions()

        APIRequestManager(
            logger = get<PlayLogger>(named(LoggerName.PLAYWRIGHT)),
            playwright = get(),
            config = get(),
            contextOptions = context
        )
    }

    factory {
        APIAction(
            logger = get<PlayLogger>(named(LoggerName.PLAYWRIGHT)),
            report = get(),
            config = get(),
            requestManager = get(),
            jsonConverter = get(),
            validationBuilder = get()
        )
    }
}

val pagesModule = module {
    factory { params ->
        WelcomePage(
            get(),
            params.getOrNull<Page>() ?: get<BrowserContext>().newPage()
        ) 
    }
    factory { params ->
        ABTestingPage(
            get(),
            params.getOrNull<Page>() ?: get<BrowserContext>().newPage()
        ) 
    }
}

val utAPIModule = module {
    factory { TaskAPICollection(get()) }
}

val mainModule = module {
    includes(
        configModule,
        utilsModule,
        playwrightModule,
        validationModel,
        guiModule,
        apiModule,
        logModule,
        reportModule,
        pagesModule,
        utAPIModule
    )
}
