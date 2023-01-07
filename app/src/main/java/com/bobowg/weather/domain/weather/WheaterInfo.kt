package com.bobowg.weather.domain.weather


data class WheaterInfo(
    val weatherDataPerDay:Map<Int,List<WeatherData>>,
    val currentWeatherData:WeatherData?
)
