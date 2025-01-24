//Tarea 3 Gonzalo Piñeiro Pombal

//En la tarea uso el sqLite para guardar los usuario agregados/contactos
//Mientras uso el room para guardar el usuario principal
//Una vez entregado y antes de emepezar la tarea 4 eliminare el sqlite de contactos por una BD room

package com.Gonzalo.aplicacionAyuda

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.os.PersistableBundle
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
import androidx.compose.ui.tooling.preview.Preview
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
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.Gonzalo.aplicacionAyuda.data.AppAyudaDBHelper
import com.Gonzalo.aplicacionAyuda.data.DATABASE_NAME
import com.Gonzalo.aplicacionAyuda.data.DATABASE_VERSION
import com.Gonzalo.aplicacionAyuda.data.MainUser
import com.Gonzalo.aplicacionAyuda.data.appAyudaDBContract
import com.Gonzalo.aplicacionAyuda.data.mainUserDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val contactNames = leerBD() // Recuperar los nombres de la base de datos
            PantallaPrincipal(contactNames)
        }


        //Sqlite
        borrarBD()
        crearBD("1", "Gonzalo", 123456789, "Spain")
        crearBD("2", "Maria", 987654321, "France")
        crearBD("3", "Carlos", 654321987, "Italy")
        leerBD()

//      Falta implementar en la aplicación la opcion de eliminar o cambiar de usuario pricipal
//        borrarBDRoom()

        //Room
        crarBDRoom()
        leerBDRoom()

    }

    //Métodos de ciclo de vida (Parte1)
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        //Borro hambas BD para el ejemplo de la tarea


        Log.d("Ciclo de vida", "llamdado a onCreate")
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


    //Funiones bases de detos + room

    private fun crearBD(contactId: String, contactName: String, contactTelephone: Int, contactNationality: String) {
        val dbHelper = AppAyudaDBHelper(this)
        val db = dbHelper.readableDatabase

        val values = ContentValues().apply {
            put(appAyudaDBContract.contactsData.ID, contactId)
            put(appAyudaDBContract.contactsData.CONTACT_NAME, contactName)
            put(appAyudaDBContract.contactsData.CONTACT_TELEPHONE, contactTelephone)
            put(appAyudaDBContract.contactsData.CONTACT_NATIONALITY, contactNationality)
        }

        val result = db.insert(appAyudaDBContract.contactsData.TABLE_NAME, null, values)

        Log.d("Database result", "Database result $result")
    }


    //Funcion que devuelve un String con los nombres de los ususarios en la base de datos
    private fun leerBD() : List<String>{
        val dbHelper = AppAyudaDBHelper(this)
        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            appAyudaDBContract.contactsData.CONTACT_NAME,
            appAyudaDBContract.contactsData.CONTACT_TELEPHONE,
            appAyudaDBContract.contactsData.CONTACT_NATIONALITY
        )

        val cursor = db.query(
            appAyudaDBContract.contactsData.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null,
            null
        )

        val names = mutableListOf<String>()
        with(cursor){
            while(cursor.moveToNext()){
                val name = getString(getColumnIndexOrThrow(appAyudaDBContract.contactsData.CONTACT_NAME))
                names.add(name)
//                Log.d("Lectura base de datos","De la base de datos $name")

            }
        }

        cursor.close()
        return names
    }

    private fun crarBDRoom(){
        val db = Room.databaseBuilder(
            applicationContext,
            mainUserDataBase::class.java, "mainUserDB"
        ).build()

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

    private fun leerBDRoom(){
        val db = Room.databaseBuilder(
            applicationContext,
            mainUserDataBase::class.java, "mainUserDB"
        ).build()

        val dao = db.userDao()

        CoroutineScope(Dispatchers.IO).launch {
            val user = dao.getAll()

            user.forEach { user ->
                Log.d("RoomDBMainUser", "Usuario: ${user.firstName} ${user.lastName1} ${user.lastName2}")
            }
        }
    }



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



    private fun borrarBD(){
        val dbHelper = AppAyudaDBHelper(this)
        val db = dbHelper.readableDatabase
        db.delete("${appAyudaDBContract.contactsData.TABLE_NAME}", null, null)
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









//@Preview
//@Composable
//fun vistaPrevia(){
//    PantallaPrincipal()
//}