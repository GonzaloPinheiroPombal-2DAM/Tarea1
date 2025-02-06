package com.Gonzalo.aplicacionAyuda.ui.screens.mainScreen


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Gonzalo.aplicacionAyuda.data.ContactData
import com.Gonzalo.aplicacionAyuda.data.mainUserDataBase
import com.Gonzalo.aplicacionAyuda.data.network.WheatherApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val db: mainUserDataBase, // Inyección de la BD
    private val weatherService: WheatherApiService // Inyección de Retrofit
) : ViewModel() {


    // Flow para actualizar los contactos en la UI
    private val _contactNames = MutableStateFlow<List<String>>(emptyList())
    val contactNames: StateFlow<List<String>> = _contactNames


    // Flow para actualizar los datos del clima
    private val _weatherState = MutableStateFlow<String>("Loading weather...")
    val weatherState: StateFlow<String> = _weatherState

    init {
        leerNombresContactosBDRoom()
        fetchWeather(42.2314, -8.7124) // Vigo
    }

    private fun leerNombresContactosBDRoom() {
        viewModelScope.launch(Dispatchers.IO) {
            _contactNames.value = db.contactDataDao().getAllContactNames()
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

    // Agregar un nuevo contacto
    fun agregarContacto(contactName: String, contactTelephone: Int, contactNationality: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val nuevoContacto = ContactData(
                contactName = contactName,
                contactTelephone = contactTelephone,
                contactNationality = contactNationality
            )
            db.contactDataDao().insertContact(nuevoContacto)
            leerNombresContactosBDRoom() // Actualiza la lista
            Log.d("RoomDBContact", "Contacto insertado en Room")
        }
    }
}




//Codigo antes de meter hilt, ignorar
//
//    // Borrar todos los usuarios
//    fun borrarBDRoom() {
//        viewModelScope.launch(Dispatchers.IO) {
//            userDao.deleteAll()
//            Log.d("RoomDBMainUser", "Todos los usuarios han sido eliminados de Room")
//        }
//    }
//
//    // Leer el usuario principal
//    fun leerMainUserBDRoom() {
//        viewModelScope.launch(Dispatchers.IO) {
//            val users = userDao.getAll()
//            users.forEach { user ->
//                Log.d("RoomDBMainUser", "Usuario: ${user.firstName} ${user.lastName1} ${user.lastName2}")
//            }
//        }
//    }
//
//    // Crear usuario principal en la BD
//    fun crearUsuarioMainBDRoom() {
//        viewModelScope.launch(Dispatchers.IO) {
//            val user = MainUser(
//                firstName = "Gonzalo",
//                lastName1 = "Piñeiro",
//                lastName2 = "Pombal",
//                phone = "123456789",
//                email = "gonzalo@gmail.com"
//            )
//            userDao.insertAll(user)
//            Log.d("RoomDBMainUser", "Usuario main insertado en Room")
//        }
//    }
//}



