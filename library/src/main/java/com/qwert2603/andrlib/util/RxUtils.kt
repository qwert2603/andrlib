package com.qwert2603.andrlib.util

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function

fun <T, U> Single<List<T>>.mapList(mapper: (T) -> U): Single<List<U>> = this
        .map { it.map(mapper) }

fun <T> Single<T>.mapError(mapper: (Throwable) -> Throwable): Single<T> = this
        .onErrorResumeNext { Single.error(mapper(it)) }

fun <T> Observable<T>.mapError(mapper: (Throwable) -> Throwable?): Observable<T> = this
        .onErrorResumeNext { t: Throwable ->
            mapper(t)
                    ?.let { Observable.error<T>(it) }
                    ?: Observable.empty<T>()
        }

/**
 * Cancel Observable when [anth] emits item.
 * If [anth] emits items before Observable emit first item, it will not trigger cancellation.
 */
fun <T, U> Observable<T>.cancelOn(anth: Observable<U>, cancelItem: T): Observable<T> {
    val completed = Exception()
    val shared = this.publish().refCount(2)
    return shared
            .materialize()
            .map { if (it.isOnComplete) throw completed else it }
            .dematerialize<T>()
            .mergeWith(anth
                    .skipUntil(shared)
                    .materialize()
                    .filter { it.value != null }
                    .map { cancelItem }
            )
            .takeUntil { it === cancelItem }
            .mapError { it.takeIf { it !== completed } }
}

fun <T> Observable<T>.pausable(isOn: Observable<Boolean>): Observable<T> {
    abstract class Message

    val itemOn = object : Message() {}
    val itemOff = object : Message() {}

    class Item(val item: T) : Message()

    return Observable
            .merge(
                    this.map { Item(it) },
                    isOn.startWith(false).map { if (it) itemOn else itemOff }
            )
            .serialize()
            .concatMap(object : Function<Message, Observable<T>> {
                private val buffer = mutableListOf<T>()
                private var on = false
                override fun apply(t: Message): Observable<T> = when (t) {
                    itemOn -> {
                        on = true
                        val b = ArrayList(buffer)
                        buffer.clear()
                        Observable.fromIterable(b)
                    }
                    itemOff -> {
                        on = false
                        Observable.empty<T>()
                    }
                    is Item -> {
                        if (on) {
                            Observable.just(t.item)
                        } else {
                            buffer += t.item
                            Observable.empty()
                        }
                    }
                    else -> null!!
                }
            })
}

fun Disposable.addTo(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}