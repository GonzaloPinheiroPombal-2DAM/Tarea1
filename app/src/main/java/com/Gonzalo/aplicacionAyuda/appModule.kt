package com.Gonzalo.aplicacionAyuda


import android.content.Context
import androidx.room.Room
import com.Gonzalo.aplicacionAyuda.data.mainUserDataBase
import com.Gonzalo.aplicacionAyuda.data.network.WheatherApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Proporcionar la base de datos Room
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): mainUserDataBase {
        return Room.databaseBuilder(
            context,
            mainUserDataBase::class.java,
            "mainUserDB"
        )
            .addMigrations(MIGRATION_2_3, MIGRATION_3_4, MIGRATION_2_4)
            .build()
    }

    // Proporcionar Retrofit para obtener el clima
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(json.asConverterFactory(MediaType.get("application/json")))
            .build()
    }

    // Proporcionar la API del clima
    @Provides
    @Singleton
    fun provideWeatherApiService(retrofit: Retrofit): WheatherApiService {
        return retrofit.create(WheatherApiService::class.java)
    }
}
