package com.bobowg.weather.data.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.bobowg.weather.data.remote.WeatherDataDto
import com.bobowg.weather.data.remote.WeatherDto
import com.bobowg.weather.domain.weather.WeatherData
import com.bobowg.weather.domain.weather.WeatherInfo
import com.bobowg.weather.domain.weather.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private data class IndexedWeatherData(
    val index:Int,
    val data:WeatherData
)


@RequiresApi(Build.VERSION_CODES.O)
fun WeatherDataDto.toWheatherDataMap(): Map<Int, List<WeatherData>> {
    return time.mapIndexed { index, time ->
        val temperature = temperatures[index]
        val weatherCode = weatherCodes[index]
        val windSpeed = windSpeeds[index]
        val pressure = pressures[index]
        val humidity = humidities[index]

            IndexedWeatherData(
                index = index,
                data =  WeatherData(
                    time = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME),
                    temperatureCelsius = temperature,
                    pressure = pressure,
                    windSpeed = windSpeed,
                    humidity = humidity,
                    weatherType = WeatherType.fromWMO(weatherCode)
                )
            )
    }.groupBy {
       it.index / 24
    }.mapValues {
        it.value.map {
            it.data
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun WeatherDto.toWeatherInfo(): WeatherInfo {
    val weatherDataMap = weatherData.toWheatherDataMap()
    val now = LocalDateTime.now()
    val currentWeatherData = weatherDataMap[0]?.find {
        val hour = if (now.minute< 30) now.hour else now.hour + 1
        it.time.hour == hour
    }
    return WeatherInfo(
        weatherDataPerDay = weatherDataMap,
        currentWeatherData = currentWeatherData
    )
}