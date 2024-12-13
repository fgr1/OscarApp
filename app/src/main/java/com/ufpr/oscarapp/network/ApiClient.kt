package com.ufpr.oscarapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val SERVER_BASE_URL = "http://192.168.136.236:3000/"
    private const val EXTERNAL_FILM_URL = "http://wecodecorp.com.br/"

    val serverInstance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(SERVER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val externalInstance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(EXTERNAL_FILM_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
