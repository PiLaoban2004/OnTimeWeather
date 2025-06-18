package com.ontimeweather.android.logic.network

import com.ontimeweather.android.OnTimeApplication
import com.ontimeweather.android.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {

    //https://api.caiyunapp.com/v2/place?query=长沙&token={q9OOkqtrWu7CIObE}&lang=zh_CN
    @GET("v2/place?token=${OnTimeApplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>
}