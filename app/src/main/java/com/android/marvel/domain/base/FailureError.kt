package com.android.marvel.domain.base

sealed class FailureError {

    /** Base Failure Errors */
    object InvalidApiKeyOrHashOrTimestamp : FailureError()
    object Mapping : FailureError()
    object Network : FailureError()
    object InvalidReferOrHash : FailureError()
    object Generic : FailureError()
}