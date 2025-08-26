package io.github.kinasr.playwright_demo_maven.config

fun <T> getEnvOrDefault(envVarName: String, fileValue: T?, defaultValue: T): T {
    val envValue = System.getenv(envVarName)
    return when {
        envValue != null -> {
            @Suppress("UNCHECKED_CAST")
            when (defaultValue) {
                is Boolean -> envValue.toBoolean() as T
                is Double -> envValue.toDouble() as T
                is Int -> envValue.toInt() as T
                else -> envValue as T
            }
        }

        fileValue != null -> fileValue
        else -> defaultValue
    }
}