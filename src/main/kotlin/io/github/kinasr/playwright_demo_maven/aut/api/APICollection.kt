package io.github.kinasr.playwright_demo_maven.aut.api

import io.github.kinasr.playwright_demo_maven.playwright_manager.api.action.APIAction
import org.koin.core.scope.Scope

abstract class APICollection(
    protected val action: APIAction
){
    abstract val serviceName: String
}