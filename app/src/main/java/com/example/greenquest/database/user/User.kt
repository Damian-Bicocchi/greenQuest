package com.example.greenquest.database.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "user_name") val userName: String?,
    @ColumnInfo(name = "password") val password: String?,
    @ColumnInfo(name = "puntos", defaultValue = "0") var puntos: Int = 0,
    @ColumnInfo(name = "monedas", defaultValue = "0") val monedas: Int = 0,
    @ColumnInfo(name = "imagen") val imagen: String? = null
)