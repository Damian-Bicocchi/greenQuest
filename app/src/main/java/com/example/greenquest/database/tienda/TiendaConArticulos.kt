package com.example.greenquest.database.tienda

import androidx.room.Embedded
import androidx.room.Relation

data class TiendaConArticulos(
    @Embedded val tienda: TiendaAdquiridos,
    @Relation(
        parentColumn = "userId",
        entityColumn = "usuarioPropietarioId"
    )
    val articulos: List<CompraArticulo>
)