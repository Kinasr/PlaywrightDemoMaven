package io.github.kinasr.playwright_demo_maven.playwright_manager.gui.manager

import com.microsoft.playwright.BrowserContext
import com.microsoft.playwright.Page
import io.github.kinasr.playwright_demo_maven.utils.logger.PlayLogger
import org.koin.mp.ThreadLocal

class PageManager(
    private val logger: PlayLogger,
    private val context: BrowserContext
) {
    companion object {
        private val pages: ThreadLocal<MutableList<Page>> = ThreadLocal()
    }
    
    fun newPage(): Page {
        val newPage = context.newPage()
        
        pages.get()?.apply { this.add(newPage) } ?: run { 
            pages.set(mutableListOf(newPage))
        }
        return newPage
    }
    
    
}