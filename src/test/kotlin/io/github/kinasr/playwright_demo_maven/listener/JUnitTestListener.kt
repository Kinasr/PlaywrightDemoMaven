package io.github.kinasr.playwright_demo_maven.listener

import com.microsoft.playwright.BrowserContext
import io.github.kinasr.playwright_demo_maven.playwright_manager.gui.manager.BrowserContextManager
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

/**
 * Full Execution Order
 * BeforeEachCallback -> @BeforeEach -> BeforeTestExecutionCallback ->
 * @Test ->
 * AfterTestExecutionCallback -> @AfterEach -> AfterEachCallback
 */
class JUnitTestListener : KoinComponent, AfterEachCallback {

    override fun afterEach(context: ExtensionContext?) {
        get<BrowserContextManager>().close()
    }
}