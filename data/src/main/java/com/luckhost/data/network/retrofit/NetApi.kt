package com.luckhost.data.network.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetApi {
    private const val BASE_URL_ADDRESS = "http://192.168.1.6:8000/api"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_ADDRESS)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitApi = retrofit.create(ApiService::class.java)
}