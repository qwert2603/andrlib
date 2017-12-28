package com.qwert2603.andr_lib.base.mvi.load_refresh

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.qwert2603.andr_lib.R
import com.qwert2603.andr_lib.util.showIfNotYet
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.view.RxView
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
        when {
            vs.lrModel.loading -> LR_ViewAnimator.showIfNotYet(LAYER_LOADING)
            vs.lrModel.loadingError != null -> LR_ViewAnimator.showIfNotYet(LAYER_ERROR)
            else -> LR_ViewAnimator.showIfNotYet(LAYER_MODEL)
        }

        if (this.isRefreshing != vs.lrModel.refreshing) this.isRefreshing = vs.lrModel.refreshing
        if (this.isEnabled != vs.lrModel.canRefresh) this.isEnabled = vs.lrModel.canRefresh
    }
}