package com.example.greenquest.dao
import androidx.room.*
import com.example.greenquest.database.user.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user LIMIT 1")
    fun getFirstUser(): User?

    @Delete
    fun delete(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Transaction
    suspend fun incrementarPuntos(addPuntos: Int) {
        if (addPuntos <= 0) return
        val currentUser = getFirstUser()
        if (currentUser != null) {
            val nuevosPuntos = currentUser.puntos + addPuntos
            currentUser.puntos = nuevosPuntos
            updateUser(currentUser)
        }
    }

}