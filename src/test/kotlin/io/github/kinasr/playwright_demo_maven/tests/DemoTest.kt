package io.github.kinasr.playwright_demo_maven.tests

import com.microsoft.playwright.APIRequest
import com.microsoft.playwright.Playwright
import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import com.microsoft.playwright.options.RequestOptions
import io.github.kinasr.playwright_demo_maven.playwright_manager.api.APIResult
import io.github.kinasr.playwright_demo_maven.playwright_manager.api.manager.APIRequestManager
import org.junit.jupiter.api.Test
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder.request
import org.koin.core.parameter.parametersOf
import org.koin.test.KoinTest
import org.koin.test.get

class DemoTest: KoinTest {

    @Test
     fun `demo test`() {
        val playwright: Playwright = get()

//        val request = playwright.request().newContext(APIRequest.NewContextOptions().apply { 
//            baseURL = ""
//            extraHTTPHeaders = mapOf()
//            
//        })

//        val apiRequest : APIRequestManager = get()
        val apiRequest: APIRequestManager = get {
            parametersOf(
                APIRequest.NewContextOptions()
                    .setBaseURL("abc")
            )
        }
        apiRequest.use {
            val res = it.request.get("/task")
            
            assertThat(res)
        }

        Playwright.create().request().newContext().fetch("")

//        request.dispose()
//        
//        request.post("/task", RequestOptions.create().apply {
//            setHeader("Cookie", "sessionid=abc")
//        })
    }
}
    
