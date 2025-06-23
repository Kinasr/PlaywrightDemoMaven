package io.github.kinasr.playwright_demo_maven.utils.report.manager

import io.github.kinasr.playwright_demo_maven.utils.report.allure.AllureReport
import io.github.kinasr.playwright_demo_maven.utils.report.core.Report
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object ReportFactory: KoinComponent {
    private val allureReport : AllureReport by inject()
    
    fun getReports(): List<Report> {
        return listOf(allureReport)
    }
}