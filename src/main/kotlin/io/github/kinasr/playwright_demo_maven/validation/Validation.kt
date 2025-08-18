package io.github.kinasr.playwright_demo_maven.validation

abstract class Validation(
    protected val builder: ValidationBuilder
) {
    abstract val and: ValidationBuilder
    abstract val then: ValidationBuilder
}