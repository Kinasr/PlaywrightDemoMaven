package io.github.kinasr.playwright_demo_maven.utils.report.allure

import io.github.kinasr.playwright_demo_maven.utils.report.model.TestStatus
import io.qameta.allure.model.Status

fun TestStatus.toAllureStatus(): Status {
    return when (this) {
        TestStatus.PASSED -> Status.PASSED
        TestStatus.FAILED -> Status.FAILED
        TestStatus.SKIPPED -> Status.SKIPPED
        TestStatus.BROKEN -> Status.BROKEN
    }
}