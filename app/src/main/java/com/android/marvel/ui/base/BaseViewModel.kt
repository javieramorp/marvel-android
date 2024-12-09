package com.android.marvel.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.marvel.common.infrastructure.ResourcesAccessor
import com.android.marvel.R
import com.android.marvel.domain.base.FailureError
import com.android.marvel.domain.base.Resource
import com.android.marvel.ui.base.BaseEvent.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

open class BaseViewModel @Inject constructor(private val resourcesAccessor: ResourcesAccessor): ViewModel() {

    private val eventChannel = Channel<EventObserver>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    protected fun handleError(failure: Resource.Failure) {
        when(failure.type) {
            FailureError.InvalidApiKeyOrHashOrTimestamp -> showMessage(R.string.common_error_unauthorized)
            FailureError.Mapping -> showMessage(R.string.common_error_mapping)
            FailureError.Network -> showMessage(R.string.common_error_network)
            FailureError.InvalidReferOrHash -> showMessage(R.string.common_error_generic)
            FailureError.Generic -> showMessage(R.string.common_error_generic)
        }
    }

    protected fun getString(resId: Int) = resourcesAccessor.getString(resId)

    protected fun showLoading(visibility: Boolean) = doEvent(ShowLoading(visibility))

    protected fun showMessage(resId: Int) = doEvent(ShowMessage(getString(resId)))

    //region View Model Output
    protected fun doEvent(event: EventObserver) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }
    //endregion
}