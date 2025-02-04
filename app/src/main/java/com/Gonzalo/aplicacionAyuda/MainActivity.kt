//Tarea 3 Gonzalo Piñeiro Pombal

//En la tarea uso el sqLite para guardar los usuario agregados/contactos
//Mientras uso el room para guardar el usuario principal
//Una vez entregado y antes de emepezar la tarea 4 eliminare el sqlite de contactos por una BD room

package com.Gonzalo.aplicacionAyuda

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.size
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.Gonzalo.aplicacionAyuda.data.ContactData
import com.Gonzalo.aplicacionAyuda.data.MainUser
import com.Gonzalo.aplicacionAyuda.data.mainUserDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            var nombres by remember { mutableStateOf(emptyList<String>()) }
            LaunchedEffect(Unit) {
                nombres = leerNombresContactosBDRoom()
            }
            PantallaPrincipal(nombres)
        }


//        agregarContacto("Sofia", 123456789, "Spain")
//        agregarContacto("Maria", 987654321, "France")
//        agregarContacto("Carlos", 654321987, "Italy")

//      Falta implementar en la aplicación la opcion de eliminar o cambiar de usuario pricipal
//        borrarBDRoom()

        //Room
//        crarUsuarioMainBDRoom()
        leerMainUserBDRoom()

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


    //Funcion encargada de generar un usuario main con valores por defecto
    private fun crarUsuarioMainBDRoom(){
        val db = Room.databaseBuilder(
            applicationContext,
            mainUserDataBase::class.java, "mainUserDB"
        )
            .addMigrations(MIGRATION_2_3, MIGRATION_3_4)
            .build()

        val dao = db.userDao()

        // Usuario en hilo secundario
        CoroutineScope(Dispatchers.IO).launch {
            val user = MainUser(
                firstName = "Gonzalo",
                lastName1 = "Piñeiro",
                lastName2 = "Pombal",
                phone = "123456789",
                email = "gonzalo@gmail.com"
            )
            dao.insertAll(user)
            Log.d("RoomDBMainUser", "Usuario main insertado en Room")
        }
    }


    //Funcion encargada de generar un nuevo contacto
    fun agregarContacto(contactName: String, contactTelephone: Int, contactNationality: String) {
        val db = Room.databaseBuilder(
            applicationContext,
            mainUserDataBase::class.java,
            "mainUserDB"
        )
            .addMigrations(MIGRATION_2_3, MIGRATION_3_4)
            .build()

        val dao = db.contactDataDao()

        CoroutineScope(Dispatchers.IO).launch {
            val nuevoContacto = ContactData(
                contactName = contactName,
                contactTelephone = contactTelephone,
                contactNationality = contactNationality
            )
            dao.insertContact(nuevoContacto)
            Log.d("RoomDBContact", "Contacto insertado en Room")
        }
    }


    //Funcion encargada de devolver todos los nombres de los contactos
    private suspend fun leerNombresContactosBDRoom(): List<String> {
        val db = Room.databaseBuilder(
            applicationContext,
            mainUserDataBase::class.java,
            "mainUserDB" // Asegúrate de usar el mismo nombre en toda la app
        )
            .addMigrations(MIGRATION_2_3, MIGRATION_3_4)
            .build()

        val dao = db.contactDataDao()

        return withContext(Dispatchers.IO) {
            dao.getAllContactNames()
        }
    }


    //Funcion encargada de devolver los datos de todos los contactos
    private suspend fun obtenerContactosBDRoom(): List<ContactData> {
        val db = Room.databaseBuilder(
            applicationContext,
            mainUserDataBase::class.java, "contactosBD"
        )
            .addMigrations(MIGRATION_2_3, MIGRATION_3_4)
            .build()

        val dao = db.contactDataDao()
        return withContext(Dispatchers.IO) {
            dao.getAllContacts()
        }
    }


    //Funcion encargada de borrar los datos del main user
    private fun borrarBDRoom(){
        val db = Room.databaseBuilder(
            applicationContext,
            mainUserDataBase::class.java, "mainUserDB"
        ).build()

        val dao = db.userDao()

        // Usar Dispatchers.IO para realizar la operación en un hilo secundario
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteAll()
            Log.d("RoomDBMainUser", "Todos los usuarios han sido eliminados de Room")
        }
    }


    private fun leerMainUserBDRoom(){
        val db = Room.databaseBuilder(
            applicationContext,
            mainUserDataBase::class.java, "mainUserDB"
        )
            .addMigrations(MIGRATION_2_3, MIGRATION_3_4)
            .build()

        val dao = db.userDao()

        CoroutineScope(Dispatchers.IO).launch {
            val user = dao.getAll()

            user.forEach { user ->
                Log.d("RoomDBMainUser", "Usuario: ${user.firstName} ${user.lastName1} ${user.lastName2}")
            }
        }
    }
}


//Función encargada de mostrar todo el contenido de la pestaña principal
@Composable
fun PantallaPrincipal(contactNames: List<String>) {
    Scaffold(
        topBar = { barraSuperior { it } },
        content = { innerPadding ->

            //Contenido principal
            Column(modifier = Modifier.padding(innerPadding)) {
                Text(
                    text = "A continuación encontrarás los futuros chats de la aplicación",
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                )
                listaLazy(names = contactNames)
            }
        }
    )
}



//Funcion encargada de mostrar el contenido del topBar
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun barraSuperior(onOptionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text("Ayuda a mayores") },
        actions = {
            // Menú desplegable para las opciones de navegación
            IconButton(onClick = {expanded = true}) {
                Icon(Icons.Default.MoreVert, contentDescription = "Menú de opciones")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Perfil") },
                    onClick = {
                        expanded = false
                        onOptionSelected("Opción 1")
                    }
                )
                DropdownMenuItem(
                    text = { Text("Ajustes") },
                    onClick = {
                        expanded = false
                        onOptionSelected("Opción 2")
                    }
                )
            }


            MostrarImagen()

        }
    )
}



//Funcion encargada de trabajar con una lista de 20 objetos cuadroLista "chats"
@Composable
private fun listaLazy(modifier: Modifier = Modifier, names: List<String>) {
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(items = names) { Name -> cuadroLista(name = Name) } //El metodo items funciona como un foreach de lazycolumn
    }
}



//Funcion encargada de mostrar cada uno de los "chats"
@Composable
fun cuadroLista(name: String, modifier: Modifier = Modifier) {
    var expanded = rememberSaveable(){ mutableStateOf(false) } //Val encargado de guardar el estado si pulsado o no

    //Enseñar mas - con animación
    val extraPadding by animateDpAsState(if(expanded.value) 48.dp else 0.dp, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow))

    Surface(color = MaterialTheme.colorScheme.primary, modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp))
    {
        Row (modifier = Modifier.padding(24.dp))
        {
            Column(modifier =  modifier.weight(1f).padding(bottom = extraPadding.coerceAtLeast(0.dp))){ //El coerce evita que la animación se salgo de los limites petando la aplicación
                Text("Chat")
                Text(text = name, style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold))
            }
            ElevatedButton(onClick = {expanded.value = !expanded.value}) {
                Text(if(expanded.value) "Mostrar menos" else "Mostrar mas")
            }
        }
    }
}



@Composable
fun MostrarImagen(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ayuda),
        contentDescription = "AyudaImagen",
        contentScale = ContentScale.Crop, // Escalado para ajustar el contenido
        modifier = modifier.size(40.dp)
    )
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
//    PantallaPrincipal()
//}