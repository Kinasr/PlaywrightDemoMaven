package io.github.kinasr.playwright_demo_maven.aut.api

import com.microsoft.playwright.APIRequest
import io.github.kinasr.playwright_demo_maven.di.PlaywrightTestScope
import io.github.kinasr.playwright_demo_maven.playwright_manager.api.action.APIAction
import io.github.kinasr.playwright_demo_maven.playwright_manager.api.manager.APIRequestManager
import io.github.kinasr.playwright_demo_maven.playwright_manager.api.model.APIResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import java.util.*

class TaskAPICollection(
    action: APIAction
) : APICollection(action), KoinComponent {
    override val serviceName: String = "/tasks"

    fun get(): APIResult<String> {
        return action.get(serviceName)
    }

    // if we need to modify the default context
    fun get2(): APIResult<String> {
        val scope = getKoin().createScope(
            UUID.randomUUID().toString(),
            named(PlaywrightTestScope.TEST_SCOPE)
        )

        val apiRequest: APIRequestManager by lazy {
            get {
                parametersOf(APIRequest.NewContextOptions().apply {
                    baseURL = "https://google.com"
                })
            }
        }
        scope.declare(apiRequest, allowOverride = true)

        val result = action.get(serviceName)
        scope.close()
        return result
    }
}