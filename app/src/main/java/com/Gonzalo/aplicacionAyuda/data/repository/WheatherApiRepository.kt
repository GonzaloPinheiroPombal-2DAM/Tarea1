package com.Gonzalo.aplicacionAyuda.data.repository

import com.Gonzalo.aplicacionAyuda.data.network.WeatherResponse
import com.Gonzalo.aplicacionAyuda.data.network.WheatherApiService
import javax.inject.Inject

class WheatherApiRepository @Inject constructor(
    private val apiRepository: WheatherApiService
){
    suspend fun getWheather(lat: Double, lon: Double): WeatherResponse {
        return apiRepository.getWeather(lat, lon)
    }
}