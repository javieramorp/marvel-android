package com.android.marvel.data.base

class ListMapper<A, D>(private val mapper: Mapper<A, D>) : Mapper<List<A>, List<D>> {

    override fun map(apiModels: List<A>): List<D> {
        val domainModels = ArrayList<D>()

        if (apiModels.isNotEmpty()) {
            for (model in apiModels) {
                domainModels.add(mapper.map(model))
            }
        }
        return domainModels
    }
}