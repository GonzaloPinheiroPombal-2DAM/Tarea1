package com.Gonzalo.aplicacionAyuda.data

import android.provider.BaseColumns
import com.Gonzalo.aplicacionAyuda.data.appAyudaDBContract.contactsData.TABLE_NAME

const val DATABASE_NAME = "AppAyudaDB"
const val DATABASE_VERSION = 3

object appAyudaDBContract {
    object contactsData : BaseColumns {
        const val TABLE_NAME = "ContactsData"
        const val ID = "contact_id"
        const val CONTACT_NAME = "contact_name"
        const val CONTACT_TELEPHONE = "contact_telephone"
        const val CONTACT_NATIONALITY = "contact_nationality"
    }

    const val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${contactsData.TABLE_NAME} (" +
                "${contactsData.ID} TEXT PRIMARY KEY," +
                "${contactsData.CONTACT_NAME} TEXT," +
                "${contactsData.CONTACT_TELEPHONE} INTEGER," +
                "${contactsData.CONTACT_NATIONALITY} TEXT)"

    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TABLE_NAME}"
}


