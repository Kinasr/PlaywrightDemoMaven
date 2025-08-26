package io.github.kinasr.playwright_demo_maven.tests

import io.github.kinasr.playwright_demo_maven.aut.api.TaskAPICollection
import org.junit.jupiter.api.Test
import org.koin.test.KoinTest
import org.koin.test.get

class TaskAPITest : KoinTest {

    @Test
    fun `get not found endpoint`() {
        val taskAPICollection: TaskAPICollection = get()

        taskAPICollection.get()
            .validate().isForbidden()
            .then.assert()
    }
}
    
