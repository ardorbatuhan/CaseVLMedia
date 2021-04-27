package com.ardorapps.demovl.remote

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ServiceComponent::class)
object RetrofitBuilder {

    private const val BASE_URL = "https://rickandmortyapi.com/"

    @Singleton
    @Provides
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun getPeopleService(): PeopleApi {
        return getRetrofit().create(PeopleApi::class.java)
    }
}