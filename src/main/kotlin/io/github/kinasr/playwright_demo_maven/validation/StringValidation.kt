package io.github.kinasr.playwright_demo_maven.validation

import org.opentest4j.AssertionFailedError

class StringValidation(
    builder: ValidationBuilder,
    private val actual: String?
) : Validation(builder) {
    override val and = builder
    override val then = builder
    
    fun isNull(): StringValidation {
        builder.addValidation {
            builder.performer.validation(
                "String is null",
                "String $actual is not null"
            ) {
                if (actual != null)
                    throw AssertionFailedError("String $actual is not null")
            }
        }
        return this
    }
    
    infix fun equals(expected: String): StringValidation {
        builder.addValidation {
            builder.performer.validation(
                "String is $expected",
                "String $actual is not equal to $expected"
            ) {
                if (actual != expected)
                    throw AssertionFailedError("String $actual is not equal to $expected")
            }
        }
        return this
    }
}