package com.qwert2603.andrlib.util

import android.util.Log

object LogUtils {

    private const val APP_TAG = "AASSDD"
    private const val ERROR_MSG = "ERROR!!!"

    enum class LogType {
        NONE,
        ANDROID,
        ANDROID_ERRORS,
        SOUT,
        SOUT_ERRORS
    }

    var logType = LogType.ANDROID

    fun d(msg: () -> String) {
        when (logType) {
            LogUtils.LogType.NONE -> nth()
            LogUtils.LogType.ANDROID -> Log.d(APP_TAG, msg())
            LogUtils.LogType.ANDROID_ERRORS -> nth()
            LogUtils.LogType.SOUT -> println("$APP_TAG ${msg()}")
            LogUtils.LogType.SOUT_ERRORS -> nth()
        }
    }

    fun d(msg: String) {
        d(APP_TAG, msg)
    }

    fun d(tag: String, msg: String) {
        when (logType) {
            LogUtils.LogType.NONE -> nth()
            LogUtils.LogType.ANDROID -> Log.d(tag, msg)
            LogUtils.LogType.ANDROID_ERRORS -> nth()
            LogUtils.LogType.SOUT -> println("$tag $msg")
            LogUtils.LogType.SOUT_ERRORS -> nth()
        }
    }

    @JvmOverloads
    fun e(msg: String = ERROR_MSG, t: Throwable? = null) {
        when (logType) {
            LogUtils.LogType.NONE -> nth()
            LogUtils.LogType.ANDROID -> Log.e(APP_TAG, "$msg $t", t)
            LogUtils.LogType.ANDROID_ERRORS -> Log.e(APP_TAG, "$msg $t", t)
            LogUtils.LogType.SOUT -> println("$APP_TAG $msg $t\n${t?.printStackTrace()}")
            LogUtils.LogType.SOUT_ERRORS -> println("$APP_TAG $msg $t\n${t?.printStackTrace()}")
        }
    }

    fun e(tag: String, msg: String, t: Throwable) {
        when (logType) {
            LogUtils.LogType.NONE -> nth()
            LogUtils.LogType.ANDROID -> Log.e(tag, "$msg $t", t)
            LogUtils.LogType.ANDROID_ERRORS -> Log.e(tag, "$msg $t", t)
            LogUtils.LogType.SOUT -> println("$APP_TAG $msg $t\n${t.printStackTrace()}")
            LogUtils.LogType.SOUT_ERRORS -> println("$APP_TAG $msg $t\n${t.printStackTrace()}")
        }
    }

    fun printCurrentStack() {
        when (logType) {
            LogUtils.LogType.NONE -> nth()
            LogUtils.LogType.ANDROID -> Log.v(APP_TAG, "", Exception())
            LogUtils.LogType.ANDROID_ERRORS -> nth()
            LogUtils.LogType.SOUT -> println("$APP_TAG, ${Exception().printStackTrace()}")
            LogUtils.LogType.SOUT_ERRORS -> nth()
        }
    }

    private fun nth() {}

    fun <T> withErrorLoggingOnly(action: () -> T): T {
        val prev = logType
        logType = when (prev) {
            LogUtils.LogType.NONE -> LogUtils.LogType.NONE
            LogUtils.LogType.ANDROID -> LogUtils.LogType.ANDROID_ERRORS
            LogUtils.LogType.ANDROID_ERRORS -> LogUtils.LogType.ANDROID_ERRORS
            LogUtils.LogType.SOUT -> LogUtils.LogType.SOUT_ERRORS
            LogUtils.LogType.SOUT_ERRORS -> LogUtils.LogType.SOUT_ERRORS
        }
        try {
            return action()
        } finally {
            logType = prev
        }
    }

    fun <T> allowDebugLogging(action: () -> T): T {
        val prev = logType
        logType = when (prev) {
            LogUtils.LogType.NONE -> LogUtils.LogType.NONE
            LogUtils.LogType.ANDROID -> LogUtils.LogType.ANDROID
            LogUtils.LogType.ANDROID_ERRORS -> LogUtils.LogType.ANDROID
            LogUtils.LogType.SOUT -> LogUtils.LogType.SOUT
            LogUtils.LogType.SOUT_ERRORS -> LogUtils.LogType.SOUT
        }
        try {
            return action()
        } finally {
            logType = prev
        }
    }
}
