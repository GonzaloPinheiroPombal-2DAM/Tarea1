package com.Gonzalo.aplicacionAyuda.data.repository

import com.Gonzalo.aplicacionAyuda.data.ContactData
import com.Gonzalo.aplicacionAyuda.data.mainUserDataBase
import javax.inject.Inject

class ContactsRepository @Inject constructor(
    private val contactsRepository: mainUserDataBase
) {

    fun getContacts(): List<String> {
        return contactsRepository.contactDataDao().getAllContactNames()
    }

    fun insertContact(contactName: String, contactTelephone: Int, contactNationality: String){
        val nuevoContacto = ContactData(
            contactName = contactName,
            contactTelephone = contactTelephone,
            contactNationality = contactNationality
        )
        contactsRepository.contactDataDao().insertContact(nuevoContacto)
    }

    fun deleteContacts(){
        contactsRepository.contactDataDao().deleteAllContacts()
    }

}