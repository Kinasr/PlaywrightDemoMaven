package io.github.kinasr.playwright_demo_maven.utils.logger

import ch.qos.logback.classic.pattern.ClassicConverter
import ch.qos.logback.classic.spi.ILoggingEvent

class PlayMDCConverter : ClassicConverter() {
    override fun convert(event: ILoggingEvent?): String? {
        val tag = event?.mdcPropertyMap["tag"]
        return if (!tag.isNullOrEmpty()) "[${tag}]" else ""
    }
}