package com.bobowg.weather.domain.repository

import com.bobowg.weather.domain.util.Resource
import com.bobowg.weather.domain.weather.WeatherInfo

interface WeatherRepository {
    suspend fun getWeatherData(lat:Double,long:Double):Resource<WeatherInfo>
}