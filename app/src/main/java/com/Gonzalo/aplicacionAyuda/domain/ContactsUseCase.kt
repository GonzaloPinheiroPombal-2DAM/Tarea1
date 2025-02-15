package com.Gonzalo.aplicacionAyuda.domain


import com.Gonzalo.aplicacionAyuda.data.repository.ContactsRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

//Devuelve una lista con los noombres de los contactos
class getContactsNames @Inject constructor(
    private val dbContacts: ContactsRepository
) {
    suspend operator fun invoke(): List<String> {
        return withContext(IO){
             dbContacts.getContacts()
        }
    }
}

//Crea un nuevo contacto
class InsertContactUseCase @Inject constructor(
    private val dbContacts: ContactsRepository
) {
    // Insertar un nuevo contacto
    suspend fun insertContact(name: String, phone: Int, nationality: String) {
        withContext(IO){
            dbContacts.insertContact(name, phone, nationality)
        }

    }
}

//Eliminar todos los contactos
class deleteAllContactsUseCase @Inject constructor(
    private val dbContacts: ContactsRepository
) {
    // Insertar un nuevo contacto
    suspend fun deleteContacts() {
        withContext(IO){
            dbContacts.deleteContacts()
        }

    }
}