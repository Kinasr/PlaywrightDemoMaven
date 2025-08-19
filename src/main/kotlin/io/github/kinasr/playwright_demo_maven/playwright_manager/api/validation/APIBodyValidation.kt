package io.github.kinasr.playwright_demo_maven.playwright_manager.api.validation

import io.github.kinasr.playwright_demo_maven.validation.StringValidation
import io.github.kinasr.playwright_demo_maven.validation.Validation
import io.github.kinasr.playwright_demo_maven.validation.ValidationBuilder
import io.github.kinasr.playwright_demo_maven.validation.ValidationPerformer

class APIBodyValidation<T>(
    builder: ValidationBuilder,
    private val body: T?
) : Validation(builder) {
    override val and = builder
    override val then = builder

    infix fun stringValue(value: (body: T?) -> String): StringValidation {
        return StringValidation(builder, value(body))
    }
}