package com.example.greenquest.database

import androidx.room.*

@Entity
data class TiendaAdquiridos(
    @PrimaryKey val userId: Int,
    @ColumnInfo(name = "articulos_adquiridos") var articulosAdquiridos: MutableList<Int> = mutableListOf(),
    @ColumnInfo(name = "monedas_obtenidas") var monedasObtenidas: Int = 0
)
