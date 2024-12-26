package com.android.marvel.domain.base

sealed class FailureError {

    /** Base Failure Errors */
    data object InvalidApiKeyOrHashOrTimestamp : FailureError()
    data object Mapping : FailureError()
    data object Network : FailureError()
    data object InvalidReferOrHash : FailureError()
    data object NotFound : FailureError()
    data object DatabaseError : FailureError()
    data object Generic : FailureError()
}