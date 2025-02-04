package com.Gonzalo.aplicacionAyuda.data

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase

@Entity(tableName = "main_user")
data class MainUser(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, //No debería haber mas de un usuario main, pero por si se implementa en un futuro
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name_1") val lastName1: String,
    @ColumnInfo(name = "last_name_2") val lastName2: String?,
    @ColumnInfo(name = "phone") val phone: String,
    @ColumnInfo(name = "email") val email: String
)


@Dao
interface MainUserDao {

    //Devuelve todos los usiarios en una lista
    @Query("SELECT * FROM main_user")
    fun getAll(): List<MainUser>

    // Permite insertar vario usuarios
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: MainUser)

    // Eliminar un usuario
    @Query("DELETE FROM main_user")
    fun deleteAll()
}


@Entity(tableName = "contact_data")
data class ContactData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "contact_name") val contactName: String,
    @ColumnInfo(name = "contact_telephone") val contactTelephone: Int,
    @ColumnInfo(name = "contact_nationality") val contactNationality: String)

@Dao
interface ContactDataDao {
    //Obtener una lista con todos los contactos
    @Query("SELECT * FROM contact_data")
    fun getAllContacts(): List<ContactData>

    // Obtener solo los nombres de todos los contactos
    @Query("SELECT contact_name FROM contact_data")
    fun getAllContactNames(): List<String>

    // Obtener solo los números de teléfono de todos los contactos
    @Query("SELECT contact_telephone FROM contact_data")
    fun getAllContactTelephones(): List<Int>

    // Obtener solo las nacionalidades de todos los contactos
    @Query("SELECT contact_nationality FROM contact_data")
    fun getAllContactNationalities(): List<String>


    //Eliminar un contacto en concreto por su id
    @Query("DELETE FROM contact_data WHERE id = :contactId")
    fun deleteContactById(contactId: Int)

    //Insertar (crear) un nuevo contacto
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContact(contact: ContactData)
}



//@Database(version = 4, entities = [MainUser::class])
//abstract class mainUserDataBase : RoomDatabase(){
//    abstract fun userDao(): MainUserDao
//    abstract fun ContactDataDao(): ContactDataDao
//
//}

@Database(version = 4, entities = [MainUser::class, ContactData::class])
abstract class mainUserDataBase : RoomDatabase(){
    abstract fun userDao(): MainUserDao
    abstract fun contactDataDao(): ContactDataDao
}