package io.github.kinasr.playwright_demo_maven.api

import io.github.kinasr.playwright_demo_maven.playwright_manager.api.manager.APIRequestManager

class TaskAPI(
    private val request: APIRequestManager
) {

    fun get() {
        request.use {
            val res = it.request.get("/task")


        }
    }
}