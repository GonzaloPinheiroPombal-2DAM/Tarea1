package com.Gonzalo.aplicacionAyuda.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query


interface WheatherApiService {
        @GET("v1/forecast")
        suspend fun getWeather(
            @Query("latitude") latitude: Double,
            @Query("longitude") longitude: Double,
            @Query("current_weather") currentWeather: Boolean = true
        ): WeatherResponse
}

@Serializable
data class WeatherResponse(
    val latitude: Float,
    val longitude: Float,
    @SerialName("generationtime_ms") val generationTimeMs: Float,
    @SerialName("utc_offset_seconds") val utcOffsetSeconds: Int,
    val timezone: String,
    @SerialName("timezone_abbreviation") val timezoneAbbreviation: String,
    val elevation: Float,
    @SerialName("current_weather") val currentWeather: CurrentWeather
)

@Serializable
data class CurrentWeather(
    val time: String,
    val interval: Int,
    val temperature: Float,
    val windspeed: Float,
    @SerialName("winddirection") val windDirection: Int,
    @SerialName("is_day") val isDay: Int,
    @SerialName("weathercode") val weatherCode: Int
)