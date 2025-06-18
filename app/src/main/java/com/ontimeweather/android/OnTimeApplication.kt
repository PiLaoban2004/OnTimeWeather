package com.ontimeweather.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class OnTimeApplication : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        const val TOKEN = "q9OOkqtrWu7CIObE"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}