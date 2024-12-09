package com.android.marvel.common.extensions

import com.android.marvel.ui.base.EventObserver


inline fun <reified T> EventObserver.assertIfIsEvent(): T {
    assert(this is T) { "Expected event of type ${T::class}, but got ${this::class}" }
    return this as T
}
