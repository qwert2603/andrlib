package com.qwert2603.andrlib.util

import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import java.util.concurrent.TimeUnit

class CancelOnTest {

    @Test
    fun inMiddle() {
        val interval = Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(5)
        val testObserver = TestObserver<Long>()
        interval
                .cancelOn(Observable.just("qq").delay(250, TimeUnit.MILLISECONDS), 14)
                .subscribe(testObserver)
        testObserver.awaitTerminalEvent(1, TimeUnit.SECONDS)
        testObserver.assertComplete()
        testObserver.assertValues(0, 1, 14)
    }

    @Test
    fun never() {
        val interval = Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(5)
        val testObserver = TestObserver<Long>()
        interval
                .cancelOn(Observable.never<String>(), 14)
                .subscribe(testObserver)
        testObserver.awaitTerminalEvent(1, TimeUnit.SECONDS)
        testObserver.assertComplete()
        testObserver.assertValues(0, 1, 2, 3, 4)
    }

    @Test
    fun empty() {
        val interval = Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(5)
        val testObserver = TestObserver<Long>()
        interval
                .cancelOn(Observable.empty<String>(), 14)
                .subscribe(testObserver)
        testObserver.awaitTerminalEvent(1, TimeUnit.SECONDS)
        testObserver.assertComplete()
        testObserver.assertValues(0, 1, 2, 3, 4)
    }

    @Test
    fun just() {
        val interval = Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(5)
        val testObserver = TestObserver<Long>()
        interval
                .cancelOn(Observable.just("qq"), 14)
                .subscribe(testObserver)
        testObserver.awaitTerminalEvent(1, TimeUnit.SECONDS)
        testObserver.assertComplete()
        testObserver.assertValues(0, 1, 2, 3, 4)
    }

    @Test
    fun before() {
        val interval = Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(5)
        val testObserver = TestObserver<Long>()
        interval
                .cancelOn(Observable.just("qq").delay(50, TimeUnit.MILLISECONDS), 14)
                .subscribe(testObserver)
        testObserver.awaitTerminalEvent(1, TimeUnit.SECONDS)
        testObserver.assertComplete()
        testObserver.assertValues(0, 1, 2, 3, 4)
    }

    @Test
    fun later() {
        val interval = Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(5)
        val testObserver = TestObserver<Long>()
        interval
                .cancelOn(Observable.just("qq").delay(550, TimeUnit.MILLISECONDS), 14)
                .subscribe(testObserver)
        testObserver.awaitTerminalEvent(1, TimeUnit.SECONDS)
        testObserver.assertComplete()
        testObserver.assertValues(0, 1, 2, 3, 4)
    }

    @Test
    fun completed() {
        val interval = Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(5)
        val publishSubject = PublishSubject.create<String>()
        publishSubject.onComplete()
        val testObserver = TestObserver<Long>()
        interval
                .cancelOn(publishSubject, 14)
                .subscribe(testObserver)
        testObserver.awaitTerminalEvent(1, TimeUnit.SECONDS)
        testObserver.assertComplete()
        testObserver.assertValues(0, 1, 2, 3, 4)
    }

    @Test
    fun behavior() {
        val interval = Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(5)
        val testObserver = TestObserver<Long>()
        interval
                .cancelOn(BehaviorSubject.createDefault(false), 14)
                .subscribe(testObserver)
        testObserver.awaitTerminalEvent(1, TimeUnit.SECONDS)
        testObserver.assertComplete()
        testObserver.assertValues(0, 1, 2, 3, 4)
    }

    @Test
    fun error() {
        val exception = Exception()
        val interval = Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(5)
                .concatWith(Observable.error(exception))
        val testObserver = TestObserver<Long>()
        interval
                .cancelOn(Observable.never<String>(), 14)
                .subscribe(testObserver)
        testObserver.awaitTerminalEvent(1, TimeUnit.SECONDS)
        testObserver.assertError(exception)
        testObserver.assertValues(0, 1, 2, 3, 4)
    }

    @Test
    fun cancelledBeforeError() {
        val interval = Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(5)
                .concatWith(Observable.error(Exception()))
        val testObserver = TestObserver<Long>()
        interval
                .cancelOn(Observable.just("qq").delay(250, TimeUnit.MILLISECONDS), 14)
                .subscribe(testObserver)
        testObserver.awaitTerminalEvent(1, TimeUnit.SECONDS)
        testObserver.assertComplete()
        testObserver.assertValues(0, 1, 14)
    }
}