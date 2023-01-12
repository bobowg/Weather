package com.bobowg.weather.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bobowg.weather.domain.weather.WeatherData
import com.bobowg.weather.domain.weather.WeatherInfo
import com.bobowg.weather.domain.weather.WeatherType
import com.bobowg.weather.presentation.ui.theme.DeepBlue
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt
import com.bobowg.weather.R
import com.bobowg.weather.presentation.ui.theme.DarkBlue
import com.bobowg.weather.presentation.ui.theme.WeatherTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherCard(
    state: WeatherState,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    var refreshing by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = refreshing) {
        if (refreshing) {
            delay(2000)
            refreshing = false
        }
    }
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = refreshing),
        onRefresh = {refreshing = true},
        indicator = {state, refreshTrigger -> SwipeRefreshIndicator(
            state = state,
            refreshTriggerDistance = refreshTrigger,
            scale = true,
            arrowEnabled = true,
            backgroundColor = Color.Green,
            shape = MaterialTheme.shapes.medium,
            largeIndication = true,
            elevation = 16.dp
        ) }
    ){
        state.weatherInfo?.currentWeatherData?.let { data ->
            Card(
                backgroundColor = backgroundColor,
                shape = RoundedCornerShape(10.dp),
                modifier = modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "今天: ${
                            data.time.format(
                                DateTimeFormatter.ofPattern("HH:mm")
                            )
                        }",
                        modifier = Modifier.align(Alignment.End),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Image(
                        painter = painterResource(id = data.weatherType.iconRes),
                        contentDescription = null,
                        modifier = Modifier.width(200.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "${data.temperatureCelsius}°C",
                        fontSize = 50.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = data.weatherType.weatherDesc,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        WeatherDataDisplay(
                            value = data.pressure.roundToInt(),
                            unit = "hpa",
                            icon = ImageVector.vectorResource(id = R.drawable.ic_pressure),
                            iconTint = Color.White,
                            textStyle = TextStyle(
                                color = Color.White,
                            )
                        )
                        WeatherDataDisplay(
                            value = data.humidity.roundToInt(),
                            unit = "%",
                            icon = ImageVector.vectorResource(id = R.drawable.ic_drop),
                            iconTint = Color.White,
                            textStyle = TextStyle(
                                color = Color.White,
                            )
                        )
                        WeatherDataDisplay(
                            value = data.windSpeed.roundToInt(),
                            unit = "km/h",
                            icon = ImageVector.vectorResource(id = R.drawable.ic_wind),
                            iconTint = Color.White,
                            textStyle = TextStyle(
                                color = Color.White,
                            )
                        )
                    }
                }
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun WeatherCardPreview() {
    val weatherData1 = WeatherData(
        time = LocalDateTime.now(),
        temperatureCelsius = 20.7,
        pressure = 20.7,
        windSpeed = 18.4,
        humidity = 89.01,
        weatherType = WeatherType.fromWMO(20)
    )
    val weatherData2 = WeatherData(
        time = LocalDateTime.now(),
        temperatureCelsius = 20.7,
        pressure = 20.7,
        windSpeed = 18.4,
        humidity = 89.01,
        weatherType = WeatherType.fromWMO(2)
    )
    val weatherData3 = WeatherData(
        time = LocalDateTime.now(),
        temperatureCelsius = 20.7,
        pressure = 20.7,
        windSpeed = 18.4,
        humidity = 89.01,
        weatherType = WeatherType.fromWMO(0)
    )

    val weatherInfo = WeatherInfo(
        weatherDataPerDay = mapOf(Pair(0, listOf(weatherData1, weatherData2, weatherData3))),
        currentWeatherData = weatherData1
    )
    val weatherInfo1 = WeatherInfo(
        weatherDataPerDay = mapOf(Pair(1, listOf(weatherData1, weatherData2, weatherData3))),
        currentWeatherData = weatherData1
    )
    val state = WeatherState(
        weatherInfo = weatherInfo,
        isLoading = false,
        error = null
    )
    val state1 = WeatherState(
        weatherInfo = weatherInfo1,
        isLoading = true,
        error = null
    )
    WeatherTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBlue)
        ) {
            WeatherCard(state = state, backgroundColor = DeepBlue)
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                TodayWeatherForecast(state = state)
                Spacer(modifier = Modifier.height(16.dp))
                TomorrowWeatherForecast(state = state1)
            }

        }
    }
}