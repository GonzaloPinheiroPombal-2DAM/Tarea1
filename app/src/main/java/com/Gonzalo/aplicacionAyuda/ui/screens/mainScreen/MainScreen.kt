package com.Gonzalo.aplicacionAyuda.ui.screens.mainScreen

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.Gonzalo.aplicacionAyuda.R

//Función encargada de mostrar todo el contenido de la pestaña principal
@Composable
fun PantallaPrincipal(contactNames: List<String>, viewModel: MainScreenViewModel) {
    Scaffold(
        topBar = { barraSuperior(viewModel) { it } },
        bottomBar = { MostrarTiempo(viewModel) },
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


//Funcion encargada de trabajar con una lista de 20 objetos cuadroLista "chats"
@Composable
fun listaLazy(modifier: Modifier = Modifier, names: List<String>) {
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(items = names) { Name -> cuadroLista(name = Name) } //El metodo items funciona como un foreach de lazycolumn
    }
}



//Funcion encargada de mostrar el contenido del topBar
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun barraSuperior(viewModel: MainScreenViewModel, onOptionSelected: (String) -> Unit) {
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
                    text = { Text("Agregar Contacto") },
                    onClick = {
                        expanded = false
                        viewModel.agregarContacto("Nuevo Contacto", 123456789, "Desconocida")
                    }
                )
            }

            MostrarImagen()

        }
    )
}

@Composable
fun MostrarTiempo(viewModel: MainScreenViewModel) {

    val weatherToShow by viewModel.weatherState.collectAsState()

    // Mostrar la temperatura en una BottomBar
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
    containerColor = MaterialTheme.colorScheme.primary

    ) {
        Text(
            text = weatherToShow,
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold)
        )
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


//Funcion encargada de mostrar una imagen
@Composable
fun MostrarImagen(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ayuda),
        contentDescription = "AyudaImagen",
        contentScale = ContentScale.Crop, // Escalado para ajustar el contenido
        modifier = modifier.size(40.dp)
    )
}