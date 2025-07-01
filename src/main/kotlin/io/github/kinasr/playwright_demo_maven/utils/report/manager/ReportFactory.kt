package io.github.kinasr.playwright_demo_maven.utils.report.manager

import io.github.kinasr.playwright_demo_maven.utils.report.allure.AllureReport
import io.github.kinasr.playwright_demo_maven.utils.report.core.ReportCore
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object ReportFactory: KoinComponent {
    private val allureReport : AllureReport by inject()
    
    fun reports(): List<ReportCore> {
        return listOf(allureReport)
    }
}