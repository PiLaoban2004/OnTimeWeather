package com.ontimeweather.android.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*Retrofit构建器*/
object ServiceCreator {
    private const val BASE_URL = "https://api.caiyunapp.com/"

    private val  retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

//    val placeService = retrofit.create(PlaceService::class.java)
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)

}