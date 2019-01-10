package com.qwert2603.andrlib.util

import org.junit.Assert
import org.junit.Test

class LogUtilsTest {

    @Test
    fun test() {

        var loggedErrors = 0
        LogUtils.onErrorLogged = { _, _, _ -> ++loggedErrors }

        LogUtils.logType = LogUtils.LogType.SOUT

        LogUtils.d("qwert")
        Thread.sleep(30)
        LogUtils.d("aassdd", "qwert")
        Thread.sleep(30)
        LogUtils.d { "zxcvb" }
        Thread.sleep(30)
        LogUtils.d("poiuy") { "zxcvb" }
        Thread.sleep(30)
        LogUtils.printCurrentStack()
        Thread.sleep(30)
        LogUtils.e("tag_e", "msg_ea", null)
        Thread.sleep(30)
        LogUtils.e("tag_e", "msg_es", RuntimeException())
        Thread.sleep(30)
        LogUtils.errorsFilter = { it !is RuntimeException }
        Thread.sleep(30)
        LogUtils.e("tag_e", "msg_ed", RuntimeException())
        Thread.sleep(30)
        LogUtils.e("msg_ef")
        Thread.sleep(30)
        LogUtils.e("msg_ef", Exception())


        LogUtils.d("***************************")

        LogUtils.logType = LogUtils.LogType.SOUT_ERRORS
        LogUtils.errorsFilter = { true }

        LogUtils.d("qwert")
        Thread.sleep(30)
        LogUtils.d("aassdd", "qwert")
        Thread.sleep(30)
        LogUtils.d { "zxcvb" }
        Thread.sleep(30)
        LogUtils.d("poiuy") { "zxcvb" }
        Thread.sleep(30)
        LogUtils.printCurrentStack()
        Thread.sleep(30)
        LogUtils.e("tag_e", "msg_e1", null)
        Thread.sleep(30)
        LogUtils.e("tag_e", "msg_e2", RuntimeException())
        Thread.sleep(30)
        LogUtils.errorsFilter = { it !is RuntimeException }
        Thread.sleep(30)
        LogUtils.e("tag_e", "msg_e3", RuntimeException())
        Thread.sleep(30)
        LogUtils.e()

        Assert.assertEquals(7, loggedErrors)
    }

}