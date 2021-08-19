package net.axay.fabrik.core.logging

import org.apache.logging.log4j.LogManager

/**
 * Logs a message with the [org.apache.logging.log4j.Level.INFO] level.
 */
@Suppress("NOTHING_TO_INLINE")
inline fun logInfo(msg: Any?) = LogManager.getLogger().info(msg)

/**
 * Logs a message with the [org.apache.logging.log4j.Level.WARN] level.
 */
@Suppress("NOTHING_TO_INLINE")
inline fun logWarning(msg: Any?) = LogManager.getLogger().warn(msg)

/**
 * Logs a message with the [org.apache.logging.log4j.Level.ERROR] level.
 */
@Suppress("NOTHING_TO_INLINE")
inline fun logError(msg: Any?) = LogManager.getLogger().error(msg)

/**
 * Logs a message with the [org.apache.logging.log4j.Level.FATAL] level.
 */
@Suppress("NOTHING_TO_INLINE")
inline fun logFatal(msg: Any?) = LogManager.getLogger().fatal(msg)
