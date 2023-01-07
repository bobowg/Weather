package com.bobowg.weather.presentation

import com.bobowg.weather.domain.weather.WeatherInfo

data class WeatherState(
    val weatherInfo: WeatherInfo? = null,
    val isLoading:Boolean = false,
    val error:String? = null

)
