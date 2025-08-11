package io.github.kinasr.playwright_demo_maven.tests

import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Playwright
import io.github.kinasr.playwright_demo_maven.browser.BrowserManager
import io.github.kinasr.playwright_demo_maven.config.Config
import io.github.kinasr.playwright_demo_maven.di.mainModule
import io.github.kinasr.playwright_demo_maven.utils.ScreenshotHelper
import io.github.kinasr.playwright_demo_maven.utils.report.Report
import io.github.oshai.kotlinlogging.KotlinLogging
import io.qameta.allure.Allure
import io.qameta.allure.model.Status
import io.qameta.allure.model.StepResult
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import java.io.File
import java.lang.Thread.sleep
import java.util.*

class Demo2Test : KoinTest {

    protected val browserManager: BrowserManager by inject()
    protected val screenshotHelper: ScreenshotHelper by inject()
    private val report: Report by inject()
    
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
//        Playwright.CreateOptions
        val playwright = Playwright.create()
        val browser = playwright.chromium().launch(BrowserType.LaunchOptions().setHeadless(false))
//        val ctx1 = browser.newContext()
//        val ctx2 = browser.newContext()
//        
//        val page1 = ctx1.newPage()
//        page1.navigate("https://playwright.dev/")
//        val page2 = ctx2.newPage().navigate("https://google.com/")
//        
//        page1.locator("", Page.LocatorOptions())


        val page = browser.newPage()

        page.onLoad {
            print("000000000000000000000")
        }

        page.navigate("https://playwright.dev/")
        page.waitForSelector("text=Get Started")
        page.click("text=Get Started")
        page.locator("").click()

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

    @Test
    fun t003() {
        val uuid01 = UUID.randomUUID().toString()
        val uuid02 = UUID.randomUUID().toString()

        val lifecycle = Allure.getLifecycle()

        lifecycle.startStep(uuid01, StepResult().apply {
            name = "AAA"
            status = Status.FAILED
        })

        lifecycle.startStep(uuid01, uuid02, StepResult().apply {
            name = "BBB"
            status = Status.FAILED
        })

        lifecycle.stopStep(uuid01)
        lifecycle.stopStep(uuid02)

        report.step("0000000000").use {
            sleep(5000)
            it.updateStatus(Status.PASSED)
        }
        
        report.step("1111111111")
            .passed()

        report.step("9999999999").use {
            sleep(5000)
            it.updateStatus(Status.BROKEN)
        }
    }
}