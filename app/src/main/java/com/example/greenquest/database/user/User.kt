package com.example.greenquest.database.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.greenquest.apiParameters.TipoResiduo

@Entity
data class User(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "user_name") val userName: String?,
    @ColumnInfo(name = "password") val password: String?,
    @ColumnInfo(name = "descripcion") var descripcion: String?,
    @ColumnInfo(name = "puntos", defaultValue = "0") var puntos: Int = 0,
    @ColumnInfo(name = "monedas", defaultValue = "0") var monedas: Int = 0,
    @ColumnInfo(name = "cant_cartones", defaultValue = "0") var cantCartones: Int = 0,
    @ColumnInfo(name= "cant_papeles", defaultValue = "0") var cantPapeles: Int = 0,
    @ColumnInfo(name= "cant_metal", defaultValue = "0") var cantMetal: Int = 0,
    @ColumnInfo(name= "cant_vidrio", defaultValue = "0") var cantVidrio: Int = 0,
    @ColumnInfo(name = "cant_plastico", defaultValue = "0") var cantPlastico: Int = 0,
    @ColumnInfo(name = "imagen") var imagen: Int? = null,
    @ColumnInfo(name = "sesion_activa") var sesionActiva: Boolean = false,
    @ColumnInfo(name = "articulos_adquiridos") var articulosAdquiridos: MutableList<Int> = mutableListOf()

){
    fun incrementarCantidadResiduo(tipoResiduo: TipoResiduo, cantidad: Int? = 1){
        when (tipoResiduo) {
            TipoResiduo.CARTON -> {
                cantCartones += cantidad ?: 1
            }
            TipoResiduo.PAPEL -> {
                cantPapeles += cantidad ?: 1
            }
            TipoResiduo.METAL -> {
                cantMetal += cantidad ?: 1
            }
            TipoResiduo.VIDRIO -> {
                cantVidrio += cantidad ?: 1
            }
            else -> {
                cantPlastico += cantidad ?: 1
            }
        }
    }
}
