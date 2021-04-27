package com.ardorapps.demovl.common

import com.ardorapps.demovl.model.People
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class ListUtils {

    companion object {
        fun <T> mergeLists(first: List<T>?, second: List<T>?): List<T> {
            val list: MutableList<T> = ArrayList()
            if (first != null) {
                list.addAll(first)
            }
            if (second != null) {
                list.addAll(second)
            }
            return list
        }

        suspend fun distinctById(first: List<People>): List<People> = coroutineScope {
            withContext(Dispatchers.IO) {
                val distinctBy = async { first.distinctBy { it.id } }
                distinctBy.await()
            }
        }
    }
}