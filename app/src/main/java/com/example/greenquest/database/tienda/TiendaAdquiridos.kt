package com.example.greenquest.database.tienda

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TiendaAdquiridos(
    @PrimaryKey val userId: Int,
    @ColumnInfo(name = "monedas_obtenidas") var monedasObtenidas: Int = 0
)