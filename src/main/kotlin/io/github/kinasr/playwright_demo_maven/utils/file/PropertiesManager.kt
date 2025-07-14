package io.github.kinasr.playwright_demo_maven.utils.file

import java.io.File
import java.io.FileOutputStream
import java.util.*

object PropertiesManager {

    /**
     * Creates or replaces a .properties file in the specified directory with the given data.
     * If the file already exists, it will be deleted and a new one will be created.
     *
     * @param directoryPath The path to the directory where the properties file should be saved.
     * @param fileName The name of the properties file (e.g., "environment.properties").
     * @param data A map containing the key-value pairs to be written to the properties file.
     */
    fun createOrUpdateProperties(directoryPath: String, fileName: String, data: Map<String, String>) {
        val directory = File(directoryPath)
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val propertiesFile = File(directory, fileName)

        if (propertiesFile.exists()) {
            propertiesFile.delete()
        }

        val properties = Properties()
        for ((key, value) in data) {
            properties.setProperty(key, value)
        }

        FileOutputStream(propertiesFile).use { outputStream ->
            properties.store(outputStream, "Generated properties file")
        }
    }
}
