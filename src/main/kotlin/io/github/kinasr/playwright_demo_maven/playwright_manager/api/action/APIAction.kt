package io.github.kinasr.playwright_demo_maven.playwright_manager.api.action

import com.google.gson.Gson
import com.microsoft.playwright.impl.TargetClosedError
import com.microsoft.playwright.options.RequestOptions
import io.github.kinasr.playwright_demo_maven.config.Config
import io.github.kinasr.playwright_demo_maven.playwright_manager.api.model.APIMethod
import io.github.kinasr.playwright_demo_maven.playwright_manager.api.model.APIResult
import io.github.kinasr.playwright_demo_maven.playwright_manager.api.manager.APIRequestManager
import io.github.kinasr.playwright_demo_maven.playwright_manager.result
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import io.github.kinasr.playwright_demo_maven.utils.report.Report
import io.github.kinasr.playwright_demo_maven.utils.report.model.AttachmentType
import io.github.kinasr.playwright_demo_maven.validation.ValidationBuilder

class APIAction(
    private val logger: PlayLogger,
    private val report: Report,
    private val config: Config,
    private val requestManager: APIRequestManager,
    private val jsonConverter: Gson,
    private val validationBuilder: ValidationBuilder
) {

    fun <T> send(
        method: APIMethod,
        endpoint: String,
        bodyType: Class<T>,
        options: (RequestOptions.() -> Unit) = {}
    ): APIResult<T> {
        requestManager.use { manager ->
            val contextOptions = RequestOptions.create().apply {
                options()
                this.setMethod(method.str)
                this.setMaxRetries(config.api.maxRetries)
            }
            
            fun executeRequest(): APIResult<T> {
                return manager.request.fetch(endpoint, contextOptions)
                    .result(bodyType, jsonConverter, validationBuilder)
            }

            val logMessage = "Sending request to: ${method.str}: ${manager.baseURL}$endpoint"
            logger.info { logMessage }
            logger.apiDebug { "Request options: $contextOptions" }
            val step = report.step(logMessage)
                .attach(
                    "Request",
                    contextOptions.toString().toByteArray(),
                    AttachmentType.JSON
                )
            
            return try {
                executeRequest()
            } catch (_: TargetClosedError) {
                logger.warn { "Target closed, retrying the request." }
                manager.close()
                executeRequest()
            } catch (e: Exception) {
                logger.error { "Request failed with error: ${e.message}" }
                step.failed("Request failed", e.message)
                throw e
            }.also { result ->
                logger.info { "Request sent successfully with status code: ${result.statusCode}" }
                step.parameter("Status Code", result.status)
                step.attach("Response", result.text.toByteArray(), AttachmentType.JSON)
                step.passed()
            }
        }
    }

    fun send(
        method: APIMethod,
        endpoint: String,
        options: (RequestOptions.() -> Unit) = {}
    ): APIResult<String> {
        return send(method, endpoint, String::class.java, options)
    }
    
    fun get(endpoint: String, bodyType: Class<String>, options: (RequestOptions.() -> Unit) = {}): APIResult<String> {
        return send(APIMethod.GET, endpoint, bodyType, options)
    }
    
    fun get(endpoint: String, options: (RequestOptions.() -> Unit) = {}): APIResult<String> {
        return get(endpoint, String::class.java, options)
    }
    
    fun post(endpoint: String, bodyType: Class<String>, options: (RequestOptions.() -> Unit) = {}): APIResult<String> {
        return send(APIMethod.POST, endpoint, bodyType, options)
    }
    
    fun post(endpoint: String, options: (RequestOptions.() -> Unit) = {}): APIResult<String> {
        return post(endpoint, String::class.java, options)
    }   
    
    fun put(endpoint: String, bodyType: Class<String>, options: (RequestOptions.() -> Unit) = {}): APIResult<String> {
        return send(APIMethod.PUT, endpoint, bodyType, options)
    }
    
    fun put(endpoint: String, options: (RequestOptions.() -> Unit) = {}): APIResult<String> {
        return put(endpoint, String::class.java, options)
    }   
    
    fun delete(endpoint: String, bodyType: Class<String>, options: (RequestOptions.() -> Unit) = {}): APIResult<String> {
        return send(APIMethod.DELETE, endpoint, bodyType, options)
    }
    
    fun delete(endpoint: String, options: (RequestOptions.() -> Unit) = {}): APIResult<String> {
        return delete(endpoint, String::class.java, options)
    }
    
    fun patch(endpoint: String, bodyType: Class<String>, options: (RequestOptions.() -> Unit) = {}): APIResult<String> {
        return send(APIMethod.PATCH, endpoint, bodyType, options)
    }
    
    fun patch(endpoint: String, options: (RequestOptions.() -> Unit) = {}): APIResult<String> {
        return patch(endpoint, String::class.java, options)
    }
    
    fun head(endpoint: String, bodyType: Class<String>, options: (RequestOptions.() -> Unit) = {}): APIResult<String> {
        return send(APIMethod.HEAD, endpoint, bodyType, options)
    }
    
    fun head(endpoint: String, options: (RequestOptions.() -> Unit) = {}): APIResult<String> {
        return head(endpoint, String::class.java, options)
    }
}