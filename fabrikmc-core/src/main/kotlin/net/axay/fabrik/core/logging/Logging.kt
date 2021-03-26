package net.axay.fabrik.core.logging

import org.apache.logging.log4j.LogManager

@Suppress("NOTHING_TO_INLINE")
inline fun logInfo(msg: String) = LogManager.getLogger().info(msg)

@Suppress("NOTHING_TO_INLINE")
inline fun logWarning(msg: String) = LogManager.getLogger().warn(msg)

@Suppress("NOTHING_TO_INLINE")
inline fun logError(msg: String) = LogManager.getLogger().error(msg)

@Suppress("NOTHING_TO_INLINE")
inline fun logFatal(msg: String) = LogManager.getLogger().fatal(msg)
