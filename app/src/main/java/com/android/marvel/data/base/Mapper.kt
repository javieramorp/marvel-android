package com.android.marvel.data.base

interface Mapper<A, D> {

    fun map(model: A): D
}