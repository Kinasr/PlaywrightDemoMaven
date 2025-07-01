package io.github.kinasr.playwright_demo_maven.utils.report2.manager

import io.github.kinasr.playwright_demo_maven.utils.report2.allure.AllureReport
import io.github.kinasr.playwright_demo_maven.utils.report2.core.ReportCore
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object ReportFactory: KoinComponent {
    private val allureReport : AllureReport by inject()
    
    fun reports(): List<ReportCore> {
        return listOf(allureReport)
    }
}