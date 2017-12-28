package com.qwert2603.andr_lib.base.mvi.load_refresh.list

import com.qwert2603.andr_lib.base.mvi.PartialChange
import com.qwert2603.andr_lib.base.mvi.load_refresh.LRPartialChange
import com.qwert2603.andr_lib.base.mvi.load_refresh.LRPresenter
import com.qwert2603.andr_lib.model.IdentifiableLong
import com.qwert2603.andr_lib.model.pagination.Page
import com.qwert2603.andr_lib.schedulers.UiSchedulerProvider
import com.qwert2603.andr_lib.util.cancelOn
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

/** Presenter that loads list of items and allows pagination. */
abstract class ListPresenter<A, I, VS : ListViewState<T>, V : ListView<VS>, T : IdentifiableLong>(uiSchedulerProvider: UiSchedulerProvider)
    : LRPresenter<A, I, VS, V>(uiSchedulerProvider) {

    companion object {
        val EMPTY_LIST_MODEL = ListModel(false, null, false)
        val EMPTY_LIST_MODEL_SINGLE_PAGE = EMPTY_LIST_MODEL.copy(allItemsLoaded = true)
    }

    /**
     * @param nextPage items page from loaded next page.
     * @return new view state with changed [ListModel].
     */
    protected abstract fun VS.changeListModel(listModel: ListModel, nextPage: List<T>? = null): VS

    /** Load next page after previous page was loaded or retry loading page. */
    protected val loadNextPageIntent: Observable<Any> = intent { it.loadNextPage() }.share()

    protected abstract fun nextPageSingle(): Single<Page<T>>

    protected fun paginationChanges(): Observable<PartialChange> = loadNextPageIntent
            .switchMap {
                /**
                 * Add [.subscribeWith(PublishSubject.create<Any>())] because [reloadIntent] may be [BehaviorSubject]
                 * and emit item immediately that triggers cancelling.
                 * We need to cancel ONLY if item is emitted while loading next page.
                 */
                nextPageSingle()
                        .toObservable()
                        .map<PartialChange> { ListPartialChange.NextPageLoaded(it) }
                        .onErrorReturn { ListPartialChange.NextPageError(it) }
                        .startWith(ListPartialChange.NextPageLoading())
                        .cancelOn(Observable.merge(reloadIntent, retryIntent, refreshIntent).subscribeWith(PublishSubject.create<Any>()), ListPartialChange.NextPageCancelled())
            }

    @Suppress("UNCHECKED_CAST")
    override fun stateReducer(vs: VS, change: PartialChange): VS {
        if (change !is ListPartialChange) return super.stateReducer(vs, change)
                .let {
                    if (change is LRPartialChange.InitialModelLoaded<*>) {
                        it.changeListModel(it.listModel.copy(
                                nextPageLoading = false,
                                nextPageError = null
                        ))
                    } else it
                }
        return when (change) {
            is ListPartialChange.NextPageLoading -> vs.changeListModel(vs.listModel.copy(
                    nextPageLoading = true,
                    nextPageError = null,
                    allItemsLoaded = false
            ))

            is ListPartialChange.NextPageError -> vs.changeListModel(vs.listModel.copy(
                    nextPageLoading = false,
                    nextPageError = change.t,
                    allItemsLoaded = false
            ))
            is ListPartialChange.NextPageCancelled -> vs.changeListModel(vs.listModel.copy(
                    nextPageLoading = false,
                    nextPageError = null,
                    allItemsLoaded = false
            ))
            is ListPartialChange.NextPageLoaded<*> -> vs.changeListModel(vs.listModel.copy(
                    nextPageLoading = false,
                    nextPageError = null,
                    allItemsLoaded = change.page.allItemsLoaded
            ), nextPage = change.page.list as List<T>)
        }
    }
}