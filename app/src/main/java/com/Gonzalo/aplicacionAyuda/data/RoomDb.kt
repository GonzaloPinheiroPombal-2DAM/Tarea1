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



//@Dao
//interface MainUserDao {
//    @Query("SELECT * FROM MainUser")
//    fun getAll(): List<MainUser>
//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    fun loadAllByIds(userIds: IntArray): List<user>
//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//    "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): User
//    @Insert
//    fun insertAll(vararg users: User)
//    @Delete
//    fun delete(user: User)
//}
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


@Database(version = 2, entities = [MainUser::class])
abstract class mainUserDataBase : RoomDatabase(){
    abstract fun userDao(): MainUserDao
}