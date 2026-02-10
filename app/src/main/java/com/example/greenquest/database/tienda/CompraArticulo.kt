package com.example.greenquest.database.tienda

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "compras",
    foreignKeys = [
        ForeignKey(
            entity = TiendaAdquiridos::class,
            parentColumns = ["userId"],
            childColumns = ["usuarioPropietarioId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CompraArticulo(
    @PrimaryKey(autoGenerate = true) val idCompraArticulo: Long = 0,
    val usuarioPropietarioId: Int,
    val articuloId: Int,
    val adquirido: Boolean = true
)