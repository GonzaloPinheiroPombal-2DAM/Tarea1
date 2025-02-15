package com.Gonzalo.aplicacionAyuda.domain

import com.Gonzalo.aplicacionAyuda.data.network.WeatherResponse
import com.Gonzalo.aplicacionAyuda.data.repository.WheatherApiRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WheatherApiServiceUseCase @Inject constructor(
    private val apiRepository: WheatherApiRepository
){
    suspend operator fun invoke(lat: Double, lon: Double): WeatherResponse {
        return withContext(IO){
            apiRepository.getWheather(lat, lon)
        }
    }
}

