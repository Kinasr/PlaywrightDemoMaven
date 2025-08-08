package io.github.kinasr.playwright_demo_maven.utils.logger

import io.github.kinasr.playwright_demo_maven.config.Config
import io.github.oshai.kotlinlogging.KotlinLogging
import org.slf4j.MDC

class PlayLogger(
    val name: String
) {
    companion object {
        fun get(name: String = "main"): PlayLogger {
            return PlayLogger(name)
        }
    }

    val logger = KotlinLogging.logger(name)

    fun trace(message: () -> Any?) {
        logger.trace(message)
    }

    fun debug(message: () -> Any?) {
        logger.debug(message)
    }

    fun info(message: () -> Any?) {
        logger.info(message)
    }

    fun apiDebug(message: () -> Any?) {
        if (Config.Logging().enableAPIDebug) {
            MDC.put("tag", "API-DEBUG")
            logger.debug(message)
            MDC.remove("tag")
        }
    }

    fun performance(message: () -> Any?) {
        if (Config.Logging().enablePerformance) {
            MDC.put("tag", "PERFORMANCE")
            logger.info(message)
            MDC.remove("tag")
        }
    }

    fun warn(message: () -> Any?) {
        logger.warn(message)
    }

    fun error(message: () -> Any?) {
        logger.error(message)
    }

    fun error(throwable: Throwable, message: () -> Any?) {
        logger.error(throwable, message)
    }
}