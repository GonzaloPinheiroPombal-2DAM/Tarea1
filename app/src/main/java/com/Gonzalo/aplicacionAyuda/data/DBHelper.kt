package com.Gonzalo.aplicacionAyuda.data


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.Gonzalo.aplicacionAyuda.data.appAyudaDBContract.SQL_CREATE_ENTRIES
import com.Gonzalo.aplicacionAyuda.data.appAyudaDBContract.SQL_DELETE_ENTRIES

class AppAyudaDBHelper(context: Context) : SQLiteOpenHelper(
    /*context = */ context,
    /*name = */ DATABASE_NAME,
    /*factory = */ null,
    /*version = */ DATABASE_VERSION){

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

}



object appAyudaContract{

}