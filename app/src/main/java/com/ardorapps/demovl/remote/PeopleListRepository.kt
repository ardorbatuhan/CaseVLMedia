package com.ardorapps.demovl.remote

import javax.inject.Inject

class PeopleListRepository @Inject constructor(
    private val peopleApiHelper: PeopleApiHelper
) {

    suspend fun getPeople(page: Int) = peopleApiHelper.getPeople(page)

}