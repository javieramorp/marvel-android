package com.android.marvel.data.base

class HttpCode {

    companion object {
        const val MISSING_API_KEY_OR_HASH_OR_TIMESTAMP = 409
        const val INVALID_REFER_OR_HASH = 404
        const val METHOD_NOT_ALLOWED = 405
        const val FORBIDDEN = 403
    }
}