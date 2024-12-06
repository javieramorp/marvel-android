package com.android.marvel.ui.base

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.marvel.R
import com.android.marvel.ui.base.BaseEvent.ShowLoading
import com.android.marvel.ui.base.BaseEvent.ShowMessage

open class BaseFragment : Fragment() {

    private var baseProgressBar: BaseProgressBar? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        baseProgressBar = BaseProgressBar(activity, R.color.yellow)
    }

    override fun onDestroy() {
        baseProgressBar?.removeView()
        super.onDestroy()
    }

    fun handleEvent(event: EventObserver) {
        when(event) {
            is ShowMessage -> activity?.let { Toast.makeText(it, event.message, Toast.LENGTH_LONG).show() }
            is ShowLoading -> baseProgressBar?.isShowing = event.visibility
        }
    }
}