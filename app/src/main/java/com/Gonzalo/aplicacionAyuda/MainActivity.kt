//Tarea 4 Gonzalo Piñeiro Pombal


//Cambios realizados:
//Modificada la estructura del proyecto(Agregada carpeta screens + sus viewModels)
//Agregadas dependencias hilt
//Agregado viewModel para el mainScreen con su logica
//Usar retrofit con una api y mostrarlo


//Falta
//Dar uso a hilt
//Añadir un boton para añadir usuario
//Borro los .kt de sqlite?





package com.Gonzalo.aplicacionAyuda

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.Gonzalo.aplicacionAyuda.data.network.WheatherApiService
import com.Gonzalo.aplicacionAyuda.ui.screens.mainScreen.MainScreenViewModel
import com.Gonzalo.aplicacionAyuda.ui.screens.mainScreen.PantallaPrincipal
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit



@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainScreenViewModel by viewModels() // Hilt inyecta el ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val datosContactos by viewModel.contactNames.collectAsState(initial = emptyList())
            PantallaPrincipal(datosContactos, viewModel)
        }
    }


    override fun onStart(){
        super.onStart()
        Log.d("Ciclo de vida", "llamado a onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Ciclo de vida", "llamado a onResume()")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Ciclo de vida", "llamdado a onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Ciclo de vida", "llamdado a onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Ciclo de vida", "llamdado a onDestroy")

    }

    override fun onRestart() {
        super.onRestart()
        Log.d("Ciclo de vida", "llamado a onRestart")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.d("Ciclo de vida", "llamdado a onLowMemory")
    }





//    //Funcion encargada de devolver todos los nombres de los contactos
//    private suspend fun leerNombresContactosBDRoom(): List<String> {
//        val db = Room.databaseBuilder(
//            applicationContext,
//            mainUserDataBase::class.java,
//            "mainUserDB" // Asegúrate de usar el mismo nombre en toda la app
//        )
//            .addMigrations(MIGRATION_2_3, MIGRATION_3_4)
//            .build()
//
//        val dao = db.contactDataDao()
//
//        return withContext(Dispatchers.IO) {
//            dao.getAllContactNames()
//        }
//    }
//
//
//    //Funcion encargada de devolver los datos de todos los contactos
//    private suspend fun obtenerContactosBDRoom(): List<ContactData> {
//        val db = Room.databaseBuilder(
//            applicationContext,
//            mainUserDataBase::class.java, "contactosBD"
//        )
//            .addMigrations(MIGRATION_2_3, MIGRATION_3_4)
//            .build()
//
//        val dao = db.contactDataDao()
//        return withContext(Dispatchers.IO) {
//            dao.getAllContacts()
//        }
//    }



}


val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Crear la nueva tabla 'contact_data'
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `contact_data` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `contact_name` TEXT NOT NULL,
                `contact_telephone` INTEGER NOT NULL,
                `contact_nationality` TEXT NOT NULL
            )
            """.trimIndent()
        )
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // No hay cambios en la estructura
    }
}




//@Preview
//@Composable
//fun vistaPrevia(){
//    lateinit var viewModel: MainScreenViewModel
//    viewModel = MainScreenViewModel(applicationContext)
//
//    val datosContactos by viewModel.contactNames.collectAsState(initial = emptyList())
//
//    PantallaPrincipal(datosContactos, viewModel)
//}



