package com.qwert2603.andrlib.base.mvi.load_refresh

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ViewAnimator
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.view.RxView
import com.qwert2603.andrlib.R
import com.qwert2603.andrlib.util.showIfNotYet
import io.reactivex.Observable
import kotlinx.android.synthetic.main.include_retry_panel.view.*
import kotlinx.android.synthetic.main.view_load_refresh.view.*

/**
 * Implementation of [LoadRefreshPanel] that contains of:
 * - [android.widget.ViewAnimator] ([LR_ViewAnimator]) to switch between layers of:
 *      1. loading indicator;
 *      2. error(retry) panel;
 *      3. model.
 * - [SwipeRefreshLayout] to allow refreshing and show refresh state. (this [LRPanelImpl] itself).
 *
 * Child view of [LRPanelImpl] added as child in XML layout is added as 3rd child (model layer) to [LR_ViewAnimator].
 * (see [LRPanelImpl.onFinishInflate]).
 */
class LRPanelImpl @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : SwipeRefreshLayout(context, attrs), LoadRefreshPanel {

    companion object {
        private const val LAYER_LOADING = 0
        private const val LAYER_ERROR = 1
        private const val LAYER_MODEL = 2
    }

    init {
        setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary)
        LayoutInflater.from(context).inflate(R.layout.view_load_refresh, this, true)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        // first 2 are CircleImageView(added by SwipeRefreshLayout) && LR_ViewAnimator.
        if (childCount == 3) {
            val child = getChildAt(2)
            removeView(child)
            LR_ViewAnimator.addView(child)
        }
        if (LR_ViewAnimator.childCount < 3) {
            // if no model view was added, add empty FrameLayout.
            // so we have 3 layers.
            LR_ViewAnimator.addView(FrameLayout(context))
        }

        if (isInEditMode) LR_ViewAnimator.showIfNotYet(LAYER_MODEL)
    }

    override fun retryClicks(): Observable<Any> = RxView.clicks(LR_Retry_Button)

    override fun refreshes(): Observable<Any> = RxSwipeRefreshLayout.refreshes(this)

    override fun render(vs: LRViewState) {
        LR_ViewAnimator.showIfNotYet(when {
            vs.lrModel.loading -> LAYER_LOADING
            vs.lrModel.loadingError != null -> LAYER_ERROR
            else -> LAYER_MODEL
        })

        if (this.isRefreshing != vs.lrModel.refreshing) this.isRefreshing = vs.lrModel.refreshing
        if (this.isEnabled != vs.lrModel.canRefresh) this.isEnabled = vs.lrModel.canRefresh
    }

    protected val _LR_ViewAnimator: ViewAnimator get() = LR_ViewAnimator
    protected val _LR_Retry_Button: Button get() = LR_Retry_Button
}