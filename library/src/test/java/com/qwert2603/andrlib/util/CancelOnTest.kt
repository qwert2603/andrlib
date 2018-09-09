package com.qwert2603.andrlib.util

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import org.junit.Test
import java.util.concurrent.TimeUnit

class CancelOnTest {

    @Test
    fun inMiddle() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(5)
                .cancelOn(Observable.just("qq").delay(250, TimeUnit.MILLISECONDS), 14)
                .test()
                .apply {
                    awaitTerminalEvent(1, TimeUnit.SECONDS)
                    assertComplete()
                    assertValues(0, 1, 14)
                }
    }

    @Test
    fun never() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(5)
                .cancelOn(Observable.never<String>(), 14)
                .test()
                .apply {
                    awaitTerminalEvent(1, TimeUnit.SECONDS)
                    assertComplete()
                    assertValues(0, 1, 2, 3, 4)
                }
    }

    @Test
    fun empty() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(5)
                .cancelOn(Observable.empty<String>(), 14)
                .test()
                .apply {
                    awaitTerminalEvent(1, TimeUnit.SECONDS)
                    assertComplete()
                    assertValues(0, 1, 2, 3, 4)
                }
    }

    @Test
    fun just() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(5)
                .cancelOn(Observable.just("qq"), 14)
                .test()
                .apply {
                    awaitTerminalEvent(1, TimeUnit.SECONDS)
                    assertComplete()
                    assertValues(0, 1, 2, 3, 4)
                }
    }

    @Test
    fun before() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(5)
                .cancelOn(Observable.just("qq").delay(50, TimeUnit.MILLISECONDS), 14)
                .test()
                .apply {
                    awaitTerminalEvent(1, TimeUnit.SECONDS)
                    assertComplete()
                    assertValues(0, 1, 2, 3, 4)
                }
    }

    @Test
    fun later() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(5)
                .cancelOn(Observable.just("qq").delay(550, TimeUnit.MILLISECONDS), 14)
                .test()
                .apply {
                    awaitTerminalEvent(1, TimeUnit.SECONDS)
                    assertComplete()
                    assertValues(0, 1, 2, 3, 4)
                }
    }

    @Test
    fun completed() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(5)
                .cancelOn(Observable.empty<Any>(), 14)
                .test()
                .apply {
                    awaitTerminalEvent(1, TimeUnit.SECONDS)
                    assertComplete()
                    assertValues(0, 1, 2, 3, 4)
                }
    }

    @Test
    fun behavior() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(5)
                .cancelOn(BehaviorSubject.createDefault(false), 14)
                .test()
                .apply {
                    awaitTerminalEvent(1, TimeUnit.SECONDS)
                    assertComplete()
                    assertValues(0, 1, 2, 3, 4)
                }
    }

    @Test
    fun error() {
        val exception = Exception()
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(5)
                .concatWith(Observable.error(exception))
                .cancelOn(Observable.never<String>(), 14)
                .test()
                .apply {
                    awaitTerminalEvent(1, TimeUnit.SECONDS)
                    assertError(exception)
                    assertValues(0, 1, 2, 3, 4)
                }
    }

    @Test
    fun cancelledBeforeError() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(5)
                .concatWith(Observable.error(Exception()))
                .cancelOn(Observable.just("qq").delay(250, TimeUnit.MILLISECONDS), 14)
                .test()
                .apply {
                    awaitTerminalEvent(1, TimeUnit.SECONDS)
                    assertComplete()
                    assertValues(0, 1, 14)
                }
    }

    @Test
    fun dontCancelFromError() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(5)
                .cancelOn(Observable.error<Any>(Exception()).delay(250, TimeUnit.MILLISECONDS), 14)
                .test()
                .apply {
                    awaitTerminalEvent(1, TimeUnit.SECONDS)
                    assertComplete()
                    assertValues(0, 1, 2, 3, 4)
                }
    }

    @Test
    fun startWith() {
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .startWith(-1L)
                .doOnSubscribe { println("CancelOnTest startWith doOnSubscribe") }
                .cancelOn(Observable.just("qq").delay(10, TimeUnit.MILLISECONDS), 14L)
                .test()
                .apply {
                    awaitTerminalEvent(1, TimeUnit.SECONDS)
                    assertComplete()
                    assertValues(-1L, 14L)
                }
    }
}