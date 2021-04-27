package com.ardorapps.demovl.remote

import javax.inject.Inject

class PeopleApiHelper @Inject constructor(
) {

    private val apiApi: PeopleApi = RetrofitBuilder.getPeopleService();

    suspend fun getPeople(page: Int) = apiApi.getPeople(page)
}