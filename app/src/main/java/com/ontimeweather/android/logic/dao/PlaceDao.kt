package com.ontimeweather.android.logic.dao

import android.content.Context
import com.google.gson.Gson
import com.ontimeweather.android.OnTimeApplication
import com.ontimeweather.android.logic.model.Place
import androidx.core.content.edit

object PlaceDao {
    fun savePlace(place: Place) {
        sharedPreferences().edit {
            putString("place", Gson().toJson(place))
        }

    }

    fun getSavedPlace() : Place {
        val placeJson = sharedPreferences().getString("place", "")
        return Gson().fromJson(placeJson, Place::class.java)
    }

    fun isPlaceSaved() = sharedPreferences().contains("place")

    private fun sharedPreferences() = OnTimeApplication.context.
            getSharedPreferences("ontime_weather", Context.MODE_PRIVATE)
}