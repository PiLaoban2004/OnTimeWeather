package com.ontimeweather.android.ui.weather

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.service.autofill.Validators.or
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ontimeweather.android.R
import com.ontimeweather.android.databinding.ActivityWeatherBinding
import com.ontimeweather.android.databinding.ForecastBinding
import com.ontimeweather.android.databinding.LifeIndexBinding
import com.ontimeweather.android.databinding.NowBinding
import com.ontimeweather.android.logic.model.RealtimeResponse
import com.ontimeweather.android.logic.model.Weather
import com.ontimeweather.android.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding
    private lateinit var nowBinding: NowBinding
    private lateinit var forecastBinding: ForecastBinding
    private lateinit var lifeIndexBinding: LifeIndexBinding

    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //初始化各个布局绑定
        initViewBindings()

        //初始化位置数据
        initLocationData()

        //观察天气数据
        observeWeatherData()

        //刷新天气信息
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
    }

    private fun observeWeatherData() {
        //观察weatherLiveData数据
        viewModel.weatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)
            }else {
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }

    private fun initLocationData() {
        //用 intent 取出经纬度坐标和地区名称并赋值到 viewModel 中
        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }
    }

    private fun initViewBindings() {
        nowBinding = NowBinding.bind(binding.includeNow.root)
        forecastBinding = ForecastBinding.bind(binding.includeForecast.root)
        lifeIndexBinding = LifeIndexBinding.bind(binding.includeLifeIndex.root)
    }

    private fun showWeatherInfo(weather: Weather) {
        nowBinding.placeName.text = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily

        //填充now.xml 布局中的数据
        val currentTempText = "${realtime.temperature.toInt()} °C"
        nowBinding.currentTemp.text = currentTempText
        nowBinding.currentSky.text = getSky(realtime.skycon).info
        val currentPM25Text = getAirQualityText(realtime)
        nowBinding.currentAQI.text = currentPM25Text
        nowBinding.nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)

        //填充forecast.xml 的数据
        forecastBinding.forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item,
                forecastBinding.forecastLayout, false)
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.text = simpleDateFormat.format(skycon.date)
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} - ${temperature.max.toInt()} °C"
            temperatureInfo.text = tempText

            //添加到父布局
            forecastBinding.forecastLayout.addView(view)
        }

        //填充life_index.xml的数据
        val lifeIndex = daily.lifeIndex
        lifeIndexBinding.coldRiskText.text = lifeIndex.coldRisk[0].desc
        lifeIndexBinding.dressingText.text = lifeIndex.dressing[0].desc
        lifeIndexBinding.ultravioletText.text = lifeIndex.ultraviolet[0].desc
        lifeIndexBinding.cadWashingText.text = lifeIndex.carWashing[0].desc
        binding.weatherLayout.visibility = View.VISIBLE

    }
    private fun getAirQualityText(realtime: RealtimeResponse.Realtime): String{
        return  try {
            realtime.airQuality?.aqi?.chn?.let { aqi ->
                "空气指数 ${aqi.toInt()}"
            } ?: "空气指数 暂无数据"
        }catch (_: Exception){
            "空气指数 获取失败"
        }
    }
}