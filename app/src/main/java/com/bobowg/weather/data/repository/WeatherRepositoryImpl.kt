package com.bobowg.weather.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.bobowg.weather.data.mappers.toWeatherInfo
import com.bobowg.weather.data.remote.WeatherApi
import com.bobowg.weather.domain.repository.WeatherRepository
import com.bobowg.weather.domain.util.Resource
import com.bobowg.weather.domain.weather.WeatherInfo
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi
) : WeatherRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo> {
        return try {
            Resource.Success(
                data = api.getWeatherData(
                    lat = lat,
                    long = long
                ).toWeatherInfo()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "一个未知的错误信息。")
        }
    }
}