package com.android.marvel.data.base

class ListMapper<A, D>(private val mapper: Mapper<A, D>) : Mapper<List<A>, List<D>> {

    override fun map(apiList: List<A>): List<D> {
        val domainModels = ArrayList<D>()

        if (apiList.isNotEmpty()) {
            for (model in apiList) {
                domainModels.add(mapper.map(model))
            }
        }
        return domainModels
    }
}