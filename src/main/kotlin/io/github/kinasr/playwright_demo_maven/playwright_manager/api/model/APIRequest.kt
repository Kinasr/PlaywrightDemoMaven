package io.github.kinasr.playwright_demo_maven.playwright_manager.api.model

import com.google.gson.Gson
import com.microsoft.playwright.options.RequestOptions

data class APIRequest(
    val method: APIMethod,
    var params: Map<String, Any>? = null,
    var headers: Map<String, String>? = null,
    var data: Any? = null,
    var timeout: Double? = null,
    var failOnStatusCode: Boolean? = null,
    var ignoreHTTPSErrors: Boolean? = null,
    var maxRedirects: Int? = null,
    var maxRetries: Int? = null,
    var form: APIForm? = null,
    var multipart: APIForm? = null
) {
    /**
     * Converts the APIRequest object to Playwright's RequestOptions.
     *
     * @param gson The Gson instance used for data serialization.
     * @return A Playwright RequestOptions object.
     */
    fun toRequestOptions(gson: Gson): RequestOptions {
        val options = RequestOptions.create()

        options.setMethod(method.str)
        this.headers?.forEach { (key, value) -> options.setHeader(key, value) }
        this.params?.forEach { (key, value) ->
            when (value) {
                is Boolean -> options.setQueryParam(key, value)
                is Int -> options.setQueryParam(key, value)
                is String -> options.setQueryParam(key, value)
            }
        }
        this.data?.let {
            when (it) {
                is String -> options.setData(it)
                is ByteArray -> options.setData(it)
                else -> {
                    // For other complex objects, serialize to JSON
                    options.setData(gson.toJson(it))
                }
            }
        }
        this.form?.let { options.setForm(it.toFormData()) }
        this.multipart?.let { options.setMultipart(it.toFormData()) }
        this.timeout?.let { options.setTimeout(it) }
        this.failOnStatusCode?.let { options.setFailOnStatusCode(it) }
        this.ignoreHTTPSErrors?.let { options.setIgnoreHTTPSErrors(it) }
        this.maxRedirects?.let { options.setMaxRedirects(it) }
        this.maxRetries?.let { options.setMaxRetries(it) }

        return options
    }

    /**
     * Converts the APIRequest object to a JSON string in a human-readable format.
     *
     * @param gson The Gson instance used for data serialization.
     * @return A JSON string that represents the APIRequest object.
     */
    fun toPrettyJsonString(gson: Gson): String {
        return gson.newBuilder().setPrettyPrinting().create().toJson(this)
    }
}
