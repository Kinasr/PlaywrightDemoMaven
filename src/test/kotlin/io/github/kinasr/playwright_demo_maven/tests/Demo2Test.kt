package io.github.kinasr.playwright_demo_maven.tests

import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Playwright
import io.github.kinasr.playwright_demo_maven.browser.BrowserManager
import io.github.kinasr.playwright_demo_maven.config.Config
import io.github.kinasr.playwright_demo_maven.di.mainModule
import io.github.kinasr.playwright_demo_maven.utils.ScreenshotHelper
import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import java.lang.Thread.sleep

class Demo2Test : KoinTest {

    protected val browserManager: BrowserManager by inject()
    protected val screenshotHelper: ScreenshotHelper by inject()
//    val config: Config by inject()

    @Test
    fun ttt() {
        startKoin {
            modules(mainModule)
        }

        browserManager.initializeBrowser()

//        println("Hello World") 
//        
//        val playwright = Playwright.create()
//        val browser = playwright.chromium().launch(BrowserType.LaunchOptions().setHeadless(false))
//        
//        val page = browser.newPage()
//        page.navigate("https://playwright.dev/")
//        page.waitForSelector("text=Get Started")

        sleep(10000)
        browserManager.closeBrowser()
    }

    @Test
    fun t001() {

        println("00000000000")
        println(Config.Browser().name)
        val playwright = Playwright.create()
        val browser = playwright.chromium().launch(BrowserType.LaunchOptions().setHeadless(false))


        val page = browser.newPage()

        page.onLoad {
            print("000000000000000000000")
        }

        page.navigate("https://playwright.dev/")
        page.waitForSelector("text=Get Started")
        page.click("text=Get Started")



        sleep(10000)
        browser.close()
    }

    @Test
    fun t002() {
        val logger = LoggerFactory.getLogger("AAAAA")
        println("00000000000")
        
        logger.info("Hi Info -----")
        logger.trace("Hi Trace -----")
        logger.debug("Hi Debug -----")
        logger.info("Hi Info -----")
        logger.warn("Hi Warn -----")
        logger.error("Hi Error -----")
        
        
        val loggerK = KotlinLogging.logger("KKKKK")
        loggerK.error { "Hi Error from Kotlin -----" }
        MDC.put("tag", "MyTag");
        logger.info("This is a tagged message");
        MDC.remove("tag");
    }
}