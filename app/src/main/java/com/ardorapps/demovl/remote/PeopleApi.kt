package com.ardorapps.demovl.remote

import com.ardorapps.demovl.model.PeopleResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PeopleApi {

    @GET("api/character")
    suspend fun getPeople(@Query("page") page: Int): PeopleResponse

}