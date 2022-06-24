package net.silkmc.silk.core.logging

import com.mojang.logging.LogUtils

/**
 * A shortcut for [LogUtils.getLogger].
 */
@Suppress("NOTHING_TO_INLINE")
inline fun logger(): org.slf4j.Logger = LogUtils.getLogger()

/**
 * Logs a message with the `INFO` level.
 */
@Suppress("NOTHING_TO_INLINE")
inline fun logInfo(msg: Any?) = LogUtils.getLogger().info(msg.toString())

/**
 * Logs a message with the `WARN` level.
 */
@Suppress("NOTHING_TO_INLINE")
inline fun logWarning(msg: Any?) = LogUtils.getLogger().warn(msg.toString())

/**
 * Logs a message with the `ERROR` level.
 */
@Suppress("NOTHING_TO_INLINE")
inline fun logError(msg: Any?) = LogUtils.getLogger().error(msg.toString())

/**
 * Logs a message with the `FATAL` level.
 */
@Deprecated("Minecraft now uses slf4j for logging, which does not provide a fatal level.", ReplaceWith("logError(msg)"))
@Suppress("NOTHING_TO_INLINE")
inline fun logFatal(msg: Any?) = LogUtils.getLogger().error(msg.toString())
