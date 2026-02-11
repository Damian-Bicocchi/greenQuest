package com.example.greenquest.dao
import androidx.room.*
import com.example.greenquest.database.user.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user LIMIT 1")
    fun getFirstUser(): User?

    @Query(
        """
        SELECT *
        FROM user
        WHERE sesion_activa = 1
    """
    )
    fun getActiveUser(): User?

    @Query("""
        UPDATE user
        SET sesion_activa = 0
        WHERE uid = :idUsuario
    """)
    fun logoutUser(idUsuario: Int)



    @Delete
    fun delete(user: User)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Transaction
    suspend fun incrementarPuntos(addPuntos: Int) {
        if (addPuntos <= 0) return
        val currentUser = getActiveUser()
        if (currentUser != null) {
            val nuevosPuntos = currentUser.puntos + addPuntos
            currentUser.puntos = nuevosPuntos
            updateUser(currentUser)
        }
    }

}