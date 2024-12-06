package com.android.marvel.ui.base

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat

class BaseProgressBar(activity: Activity?, colorResId: Int = android.R.color.white) {

    private var progressBar: ProgressBar? = null
    private var childrenView: RelativeLayout? = null
    private var rootView: ViewGroup? = null
    var isShowing: Boolean = false
        set(value) {
            field = value
            progressBar?.let {
                it.visibility = if (field) View.VISIBLE else View.INVISIBLE
            }
        }

    init {
        activity?.let { wrapActivity ->
            rootView = wrapActivity.findViewById<View>(android.R.id.content).rootView as ViewGroup
            progressBar = ProgressBar(wrapActivity, null, android.R.attr.progressBarStyleLarge).apply {
                indeterminateTintList = ContextCompat.getColorStateList(wrapActivity, colorResId)
                isIndeterminate = true
                visibility = View.INVISIBLE
            }
            val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
            childrenView = RelativeLayout(wrapActivity)
            childrenView?.gravity = Gravity.CENTER
            childrenView?.addView(progressBar)
            rootView?.addView(childrenView, params)
        }
    }

    fun removeView() {
        if (progressBar != null && childrenView != null && rootView != null) {
            isShowing = false
            childrenView?.removeView(progressBar)
            rootView?.removeView(childrenView)
            progressBar = null
            childrenView = null
            rootView = null
        }
    }
}