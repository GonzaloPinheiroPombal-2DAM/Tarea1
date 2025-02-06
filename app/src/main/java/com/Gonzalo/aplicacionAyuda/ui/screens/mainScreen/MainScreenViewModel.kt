package com.Gonzalo.aplicacionAyuda.ui.screens.mainScreen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.Gonzalo.aplicacionAyuda.MIGRATION_2_3
import com.Gonzalo.aplicacionAyuda.MIGRATION_3_4
import com.Gonzalo.aplicacionAyuda.data.ContactData
import com.Gonzalo.aplicacionAyuda.data.MainUser
import com.Gonzalo.aplicacionAyuda.data.mainUserDataBase
import com.Gonzalo.aplicacionAyuda.data.network.WheatherApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit

class MainScreenViewModel (context: Context) : ViewModel(){

    // Instancia de la base de datos
    private val db = Room.databaseBuilder(
        context.applicationContext,
        mainUserDataBase::class.java, "mainUserDB"
    ).addMigrations(MIGRATION_2_3, MIGRATION_3_4).build()


    //Daos para acceder a las bd
    private val userDao = db.userDao()
    private val contactDao = db.contactDataDao()

    // Flow para actualizar los contactos en la UI
    private val _contactNames = MutableStateFlow<List<String>>(emptyList())
    val contactNames: StateFlow<List<String>> = _contactNames


    // Flow para actualizar los datos del clima
    private val _weatherState = MutableStateFlow<String>("Loading wheather")
    val weatherState: StateFlow<String> = _weatherState

    // Uso retrofit para obtener el clima
    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")
        .addConverterFactory(json.asConverterFactory(MediaType.get("application/json")))
        .build()
    private val weatherService = retrofit.create(WheatherApiService::class.java)



    //Guardo los contactos nuevos en la bd
    init {
        leerNombresContactosBDRoom()
        fetchWeather(42.2314, -8.7124) // Vigo
    }
    private fun leerNombresContactosBDRoom() {
        viewModelScope.launch(Dispatchers.IO) {
            _contactNames.value = contactDao.getAllContactNames()
        }
    }
    fun fetchWeather(lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val weather = weatherService.getWeather(lat, lon)
                _weatherState.value = "📍 Vigo - 🌡️ ${weather.currentWeather.temperature}°C"
                Log.d("Weather", _weatherState.value)
            } catch (e: Exception) {
                _weatherState.value = "Error obteniendo clima"
                Log.e("Weather", "Error: ${e.message}")
            }
        }
    }



    //Funciones para trabajar sobre la base de datos room

    // Agregar un nuevo contacto
    fun agregarContacto(contactName: String, contactTelephone: Int, contactNationality: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val nuevoContacto = ContactData(
                contactName = contactName,
                contactTelephone = contactTelephone,
                contactNationality = contactNationality
            )
            contactDao.insertContact(nuevoContacto)
            leerNombresContactosBDRoom() // Actualiza la lista
            Log.d("RoomDBContact", "Contacto insertado en Room")
        }
    }

    // Borrar todos los usuarios
    fun borrarBDRoom() {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.deleteAll()
            Log.d("RoomDBMainUser", "Todos los usuarios han sido eliminados de Room")
        }
    }

    // Leer el usuario principal
    fun leerMainUserBDRoom() {
        viewModelScope.launch(Dispatchers.IO) {
            val users = userDao.getAll()
            users.forEach { user ->
                Log.d("RoomDBMainUser", "Usuario: ${user.firstName} ${user.lastName1} ${user.lastName2}")
            }
        }
    }

    // Crear usuario principal en la BD
    fun crearUsuarioMainBDRoom() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = MainUser(
                firstName = "Gonzalo",
                lastName1 = "Piñeiro",
                lastName2 = "Pombal",
                phone = "123456789",
                email = "gonzalo@gmail.com"
            )
            userDao.insertAll(user)
            Log.d("RoomDBMainUser", "Usuario main insertado en Room")
        }
    }
}