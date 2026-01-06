package com.example.greenquest.database.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "user_name") val userName: String?,
    @ColumnInfo(name = "password") val password: String?,
    @ColumnInfo(name = "descripcion") val descripcion: String?,
    @ColumnInfo(name = "puntos", defaultValue = "0") var puntos: Int = 0,
    @ColumnInfo(name = "monedas", defaultValue = "0") val monedas: Int = 0,
    @ColumnInfo(name = "cant_cartones", defaultValue = "0") val cant_cartones: Int = 0,
    @ColumnInfo(name= "cant_papeles", defaultValue = "0") val cant_papeles: Int = 0,
    @ColumnInfo(name= "cant_metal", defaultValue = "0") val cant_metal: Int = 0,
    @ColumnInfo(name= "cant_vidrio", defaultValue = "0") val cant_vidrio: Int = 0,
    @ColumnInfo(name = "cant_plastico", defaultValue = "0") val cant_plastico: Int = 0,
    @ColumnInfo(name = "imagen") val imagen: String? = null
)