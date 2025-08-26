package io.github.kinasr.playwright_demo_maven.playwright_manager.api.validation

import io.github.kinasr.playwright_demo_maven.playwright_manager.api.model.APIResult
import io.github.kinasr.playwright_demo_maven.validation.Validation
import io.github.kinasr.playwright_demo_maven.validation.ValidationBuilder
import org.opentest4j.AssertionFailedError

class APIValidation<T>(
    builder: ValidationBuilder,
    private val apiResult: APIResult<T>
) : Validation(builder) {
    override val and = builder
    override val then = builder

    fun isOK(): APIValidation<T> {
        return this hasStatusCode 200
    }

    fun isNotFound(): APIValidation<T> {
        return this hasStatusCode 404
    }

    fun isForbidden(): APIValidation<T> {
        return this hasStatusCode 404
    }

    infix fun hasStatusCode(statusCode: Int): APIValidation<T> {
        builder.addValidation {
            builder.performer.validation(
                "API response status code is $statusCode",
                "API response status code is not $statusCode"
            ) {
                if (apiResult.statusCode != statusCode)
                    throw AssertionFailedError("API response status code is ${apiResult.statusCode} while it should be $statusCode")
            }
        }
        return this
    }

    infix fun hasURL(url: String): APIValidation<T> {
        builder.addValidation {
            builder.performer.validation(
                "API response URL is $url",
                "API response URL is not $url"
            ) {
                if (apiResult.url != url)
                    throw AssertionFailedError("API response URL is ${apiResult.url} while it should be $url")
            }
        }
        return this
    }

    fun hasHeader(key: String, value: String): APIValidation<T> {
        builder.addValidation {
            builder.performer.validation(
                "API response header $key is $value",
                "API response header $key is not $value"
            ) {
                if (!apiResult.headers.contains(key))
                    throw AssertionFailedError("API response header $key is missing ${apiResult.headers}")
                if (apiResult.headers[key] != value)
                    throw AssertionFailedError("API response header ${key}=${apiResult.headers[key]} while it should be $value")
            }
        }
        return this
    }

    infix fun hasBody(body: T): APIValidation<T> {
        builder.addValidation {
            builder.performer.validation(
                "API response body is $body",
                "API response body is not $body"
            ) {
                if (apiResult.body != body)
                    throw AssertionFailedError("API response body is ${apiResult.body} while it should be $body")
            }
        }
        return this
    }

    fun body(): APIBodyValidation<T> {
        return APIBodyValidation(builder, apiResult.body)
    }
}