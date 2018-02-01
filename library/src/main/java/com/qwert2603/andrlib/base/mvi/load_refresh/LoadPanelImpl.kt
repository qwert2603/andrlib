package com.qwert2603.andrlib.base.mvi.load_refresh

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ProgressBar
import android.widget.ViewAnimator
import com.jakewharton.rxbinding2.view.RxView
import com.qwert2603.andrlib.R
import com.qwert2603.andrlib.util.showIfNotYet
import io.reactivex.Observable
import kotlinx.android.synthetic.main.include_retry_panel.view.*

/**
 * Implementation of [LoadRefreshPanel] that can show loading progress, error panel and model's view.
 * Must have EXACTLY 1 child (model's view).
 * Contains 3 layers:
 *  1. loading indicator ([ProgressBar]);
 *  2. error(retry) panel (errorTextView & retry [Button]);
 *  3. model (inflated from xml).
 */
class LoadPanelImpl @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewAnimator(context, attrs), LoadRefreshPanel {

    companion object {
        private const val LAYER_LOADING = 0
        private const val LAYER_ERROR = 1
        private const val LAYER_MODEL = 2
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_load, this, true)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (childCount != 3) throw Exception()
        if (isInEditMode) showIfNotYet(LAYER_MODEL)
    }

    override fun retryClicks(): Observable<Any> = RxView.clicks(LR_Retry_Button)

    override fun refreshes(): Observable<Any> = Observable.never()

    override fun render(vs: LRViewState) {
        when {
            vs.lrModel.loading -> showIfNotYet(LAYER_LOADING)
            vs.lrModel.loadingError != null -> showIfNotYet(LAYER_ERROR)
            else -> showIfNotYet(LAYER_MODEL)
        }
    }

    protected val _LR_Retry_Button: Button get() = LR_Retry_Button
}