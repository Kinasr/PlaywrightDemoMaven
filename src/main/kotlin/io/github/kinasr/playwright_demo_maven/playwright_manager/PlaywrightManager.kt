package io.github.kinasr.playwright_demo_maven.playwright_manager

import com.microsoft.playwright.Playwright
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class PlaywrightManager(
    private val logger: PlayLogger
) : AutoCloseable {

    private val playwrightPool: ConcurrentMap<Long, Playwright> = ConcurrentHashMap()

    fun initialize(options: (Playwright.CreateOptions.() -> Unit) = {}): Playwright {
        val worker = Thread.currentThread()
        val workerId = worker.threadId()

        return playwrightPool.compute(workerId) { _, playwright ->
            playwright ?: run {
                logger.info { "Initializing Playwright for thread: ${worker.name} (ID: $workerId)" }
                val op = Playwright.CreateOptions().also { it.options() }
                runCatching { Playwright.create(op) }
                    .onSuccess { logger.debug { "Playwright initialized for thread: ${worker.name}" } }
                    .onFailure {
                        logger.error { "Failed to initialize Playwright for thread: ${worker.name}: ${it.message}" }
                        throw it
                    }
                    .getOrNull()
            }
        }!!
    }
    
    @Synchronized
    fun clearPool() {
        logger.info { "Clearing Playwright pool." }
        playwrightPool.forEach { workerId, playwright -> 
            closePlaywright(workerId, playwright)
        }
    }
    
    private fun closePlaywright(threadId: Long, playwright: Playwright) {
        runCatching { playwright.close() }
            .onSuccess { logger.trace { "Playwright closed for thread id: $threadId" } }
            .onFailure { logger.warn { "Error closing Playwright for thread id: $threadId: ${it.message}" } }
    }

    override fun close() {
        clearPool()
    }
}