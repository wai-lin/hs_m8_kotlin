package org.example

import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

fun main() {
    logger.trace { "Hello World!" }
    logger.info { "Hello World!" }
    logger.debug { "Hello World!" }
    logger.warn { "Hello World!" }
    logger.error { "Hello World!" }
}