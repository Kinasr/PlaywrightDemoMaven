package io.github.kinasr.playwright_demo_maven.validation

import org.opentest4j.AssertionFailedError

class StringValidation(
    builder: ValidationBuilder,
    private val actual: String?
) : Validation(builder) {
    override val and = builder
    override val then = builder

    fun isNull(
        msg: Pair<String, String> = "String is not null" to "String $actual is null"
    ): StringValidation {
        builder.addValidation {
            builder.performer.validation(msg.first, msg.second) {
                if (actual != null)
                    throw AssertionFailedError(msg.second)
            }
        }
        return this
    }

    fun equals(
        expected: String,
        msg: Pair<String, String> = "String is $expected" to "String $actual is not equal to $expected"
    ): StringValidation {
        builder.addValidation {
            builder.performer.validation(
                msg.first,
                msg.second
            ) {
                if (actual != expected)
                    throw AssertionFailedError(msg.second)
            }
        }
        return this
    }
}