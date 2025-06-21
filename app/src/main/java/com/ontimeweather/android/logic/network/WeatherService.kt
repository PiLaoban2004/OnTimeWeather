package com.ontimeweather.android.logic.network

import com.ontimeweather.android.OnTimeApplication
import com.ontimeweather.android.logic.model.DailyResponse
import com.ontimeweather.android.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {
    //https://api.caiyunapp.com/v2.6/q9OOkqtrWu7CIObE/101.6656,39.2072/realtime.json
    @GET("v2.6/${OnTimeApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(@Path("lng") lng: String, @Path("lat") lat: String):
            Call<RealtimeResponse>

    @GET("v2.6/${OnTimeApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(@Path("lng") lng: String, @Path("lat") lat: String):
            Call<DailyResponse>
}