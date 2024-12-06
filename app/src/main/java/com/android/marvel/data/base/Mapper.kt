package com.android.marvel.data.base

interface Mapper<A, D> {

    fun map(apiModel: A): D
}