package com.example.greenquest.dao
import androidx.room.*
import com.example.greenquest.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user LIMIT 1")
    fun getFirtUser(): User?

    @Delete
    fun delete(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

}