package com.qwert2603.andrlib.util

import android.util.Log

object LogUtils {

    private const val APP_TAG = "AASSDD"
    private const val ERROR_MSG = "ERROR!!!"

    private enum class LogType {
        NONE, ANDROID, SOUT
    }

    private val logType = LogType.ANDROID

    fun d(msg: String) {
        d(APP_TAG, msg)
    }

    fun d(tag: String, msg: String) {
        when (logType) {
            LogUtils.LogType.NONE -> nth()
            LogUtils.LogType.ANDROID -> Log.d(tag, msg)
            LogUtils.LogType.SOUT -> println("$tag $msg")
        }
    }

    @JvmOverloads
    fun e(msg: String = ERROR_MSG, t: Throwable? = null) {
        when (logType) {
            LogUtils.LogType.NONE -> nth()
            LogUtils.LogType.ANDROID -> Log.e(APP_TAG, "$msg $t", t)
            LogUtils.LogType.SOUT -> println("$APP_TAG $msg $t\n${t?.printStackTrace()}")
        }
    }

    fun e(tag: String, msg: String, t: Throwable) {
        when (logType) {
            LogUtils.LogType.NONE -> nth()
            LogUtils.LogType.ANDROID -> Log.e(tag, "$msg $t", t)
            LogUtils.LogType.SOUT -> println("$APP_TAG $msg $t\n${t.printStackTrace()}")
        }
    }

    fun printCurrentStack() {
        when (logType) {
            LogUtils.LogType.NONE -> nth()
            LogUtils.LogType.ANDROID -> Log.v(APP_TAG, "", Exception())
            LogUtils.LogType.SOUT -> println("$APP_TAG, ${Exception().printStackTrace()}")
        }
    }

    private fun nth() {}
}
