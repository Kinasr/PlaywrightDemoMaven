package io.github.kinasr.playwright_demo_maven.tests

import com.microsoft.playwright.APIRequest
import com.microsoft.playwright.Playwright
import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import io.github.kinasr.playwright_demo_maven.di.PlaywrightTestScope
import io.github.kinasr.playwright_demo_maven.playwright_manager.api.action.APIAction
import io.github.kinasr.playwright_demo_maven.playwright_manager.api.manager.APIRequestManager
import org.junit.jupiter.api.Test
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.test.KoinTest
import org.koin.test.get
import java.util.UUID

class DemoTest: KoinTest {
    private lateinit var testScope: Scope

    @Test
     fun `demo test`() {
        testScope = getKoin().createScope(
            "test_${UUID.randomUUID()}",
            named(PlaywrightTestScope.TEST_SCOPE)
        )
        
        val apiRequest : APIRequestManager = get()
        testScope.declare(apiRequest, allowOverride = true)
        
        val action: APIAction = testScope.get()
        
        action.get("/tasks")
    }
}
    
