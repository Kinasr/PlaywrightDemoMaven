package io.github.kinasr.playwright_demo_maven.config

import kinasr.nsr_yaml.core.YAML
import kinasr.nsr_yaml.exception.YAMLFileException

class ConfigLoader(configFilePath: String = "src/main/resources/config.yaml") {
    var config: ConfigRecord
        private set

    init {
        try {
            config = YAML.read(configFilePath).get().`as`<ConfigRecord?>(ConfigRecord::class.java)
                ?: throw YAMLFileException("Please provide the config.yaml file under src/main/resources/")
        } catch (e: Exception) {
            throw YAMLFileException("Please provide the config.yaml file under src/main/resources/", e)
        }
    }
}