package com.android.marvel.ui.base

sealed class BaseEvent: EventObserver {
    data class ShowMessage(val message: String): BaseEvent()
    data class ShowLoading(val visibility: Boolean): BaseEvent()
}