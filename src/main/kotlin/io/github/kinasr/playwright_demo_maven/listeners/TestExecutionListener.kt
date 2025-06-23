package io.github.kinasr.playwright_demo_maven.listeners

import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import io.github.kinasr.playwright_demo_maven.utils.report.Report
import io.github.kinasr.playwright_demo_maven.utils.report.model.ReportStatus
import io.qameta.allure.Allure
import io.qameta.allure.listener.ContainerLifecycleListener
import io.qameta.allure.listener.FixtureLifecycleListener
import io.qameta.allure.listener.StepLifecycleListener
import io.qameta.allure.listener.TestLifecycleListener
import io.qameta.allure.model.Container
import io.qameta.allure.model.FixtureResult
import io.qameta.allure.model.StepResult
import io.qameta.allure.model.TestResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.testng.ITestContext
import org.testng.ITestListener
import org.testng.ITestResult
import java.util.UUID

/**
 * TestNG listener for test execution events.
 *
 * This listener integrates with the reporting system to automatically track test execution
 * and update the test report with appropriate statuses and details.
 */
class TestExecutionListener : ITestListener, KoinComponent {

    private val logger: PlayLogger by inject()
    
    private val testContainers = mutableMapOf<String, Container>()
    
    override fun onStart(context: ITestContext) {
        logger.info { "Test suite '${context.suite.name}' started" }
        // Initialize reporting system if not already initialized
        try {
            Report.initialize()
        } catch (e: Exception) {
            logger.error(e) { "Failed to initialize reporting system" }
        }
    }
    
    override fun onFinish(context: ITestContext) {
        logger.info { "Test suite '${context.suite.name}' finished" }
        // Generate and finalize the report
        try {
            Report.shutdown()
        } catch (e: Exception) {
            logger.error(e) { "Error during report generation" }
        }
    }
    
    override fun onTestStart(result: ITestResult) {
        val testName = getTestName(result)
        logger.info { "Test started: $testName" }
        
        // Start a new test in the report
        Report.startTest(
            testName = testName,
            description = result.method.description ?: ""
        )
        
        // Add test parameters to the report
        result.parameters.forEachIndexed { index, param ->
            Report.addParameter("Parameter $index", param?.toString() ?: "null")
        }
    }
    
    override fun onTestSuccess(result: ITestResult) {
        val testName = getTestName(result)
        logger.info { "Test passed: $testName" }
        Report.endTest(ReportStatus.PASSED)
    }
    
    override fun onTestFailure(result: ITestResult) {
        val testName = getTestName(result)
        logger.error(result.throwable) { "Test failed: $testName" }
        
        // Log the failure details
        result.throwable?.let { throwable ->
            Report.step("Test Failure: ${throwable.message}", ReportStatus.FAILED)
            // Attach stack trace if available
            val stackTrace = throwable.stackTraceToString()
            Report.attach("Failure Stack Trace", stackTrace, "text/plain")
        }
        
        Report.endTest(ReportStatus.FAILED)
    }
    
    override fun onTestSkipped(result: ITestResult) {
        val testName = getTestName(result)
        logger.warn { "Test skipped: $testName" }
        
        result.throwable?.let { throwable ->
            Report.step("Test Skipped: ${throwable.message}", ReportStatus.SKIPPED)
        } ?: Report.step("Test Skipped", ReportStatus.SKIPPED)
        
        Report.endTest(ReportStatus.SKIPPED)
    }
    
    override fun onTestFailedButWithinSuccessPercentage(result: ITestResult) {
        onTestFailure(result)
    }
    
    private fun getTestName(result: ITestResult): String {
        return "${result.testClass.name}.${result.method.methodName}"
    }
}

/**
 * Allure test lifecycle listener for fine-grained control over test reporting.
 */
class AllureTestLifecycleListener : TestLifecycleListener {
    override fun beforeTestStart(testResult: TestResult) {
        // Initialize test metadata if needed
    }
    
    override fun beforeTestStop(testResult: TestResult) {
        // Finalize test metadata if needed
    }
}

/**
 * Allure step lifecycle listener for custom step handling.
 */
class AllureStepLifecycleListener : StepLifecycleListener {
    override fun beforeStepStart(step: StepResult) {
        // Custom step start handling
    }
    
    override fun beforeStepStop(step: StepResult) {
        // Custom step stop handling
    }
}

/**
 * Allure fixture lifecycle listener for setup/teardown methods.
 */
class AllureFixtureLifecycleListener : FixtureLifecycleListener {
    override fun beforeFixtureStart(fixture: FixtureResult) {
        // Custom fixture start handling
    }
    
    override fun beforeFixtureStop(fixture: FixtureResult) {
        // Custom fixture stop handling
    }
}

/**
 * Allure container lifecycle listener for test containers.
 */
class AllureContainerLifecycleListener : ContainerLifecycleListener {
    override fun beforeContainerStart(container: Container) {
        // Custom container start handling
    }
    
    override fun beforeContainerStop(container: Container) {
        // Custom container stop handling
    }
}
