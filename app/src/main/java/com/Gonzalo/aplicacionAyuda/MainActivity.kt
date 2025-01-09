//Tarea 2 Gonzalo Piñeiro Pombal
package com.Gonzalo.aplicacionAyuda

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



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PantallaPrincipal()
        }
    }

    //Métodos de ciclo de vida (Parte1)
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
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
}


//Función encargada de mostrar todo el contenido de la pestaña principal
@Composable
fun PantallaPrincipal() {
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
                listaLazy()
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
private fun listaLazy(modifier: Modifier = Modifier, names: List<String> = List(20){"$it"}) {
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






@Preview
@Composable
fun vistaPrevia(){
    PantallaPrincipal()
}