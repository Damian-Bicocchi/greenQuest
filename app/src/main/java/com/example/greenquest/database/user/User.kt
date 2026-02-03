package com.example.greenquest.database.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.greenquest.apiParameters.TipoResiduo
import com.example.greenquest.database.TiendaAdquiridos

@Entity
data class User(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "user_name") val userName: String?,
    @ColumnInfo(name = "password") val password: String?,
    @ColumnInfo(name = "descripcion") var descripcion: String?,
    @ColumnInfo(name = "puntos", defaultValue = "0") var puntos: Int = 0,
    @ColumnInfo(name = "monedas", defaultValue = "0") var monedas: Int = 0,
    @ColumnInfo(name = "cant_cartones", defaultValue = "0") var cant_cartones: Int = 0,
    @ColumnInfo(name= "cant_papeles", defaultValue = "0") var cant_papeles: Int = 0,
    @ColumnInfo(name= "cant_metal", defaultValue = "0") var cant_metal: Int = 0,
    @ColumnInfo(name= "cant_vidrio", defaultValue = "0") var cant_vidrio: Int = 0,
    @ColumnInfo(name = "cant_plastico", defaultValue = "0") var cant_plastico: Int = 0,
    @ColumnInfo(name = "imagen") var imagen: Int? = null,
    @ColumnInfo(name = "articulos_adquiridos") var articulos_adquiridos: MutableList<Int> = mutableListOf()

){
    fun incrementarCantidadResiduo(tipoResiduo: TipoResiduo, cantidad: Int? = 1){
        if(tipoResiduo == TipoResiduo.CARTON){
            cant_cartones +=cantidad?:1
        }else if(tipoResiduo == TipoResiduo.PAPEL){
            cant_papeles +=cantidad?:1
        }else if(tipoResiduo == TipoResiduo.METAL){
            cant_metal +=cantidad?:1
        }else if (tipoResiduo == TipoResiduo.VIDRIO){
            cant_vidrio +=cantidad?:1
        }else{
            cant_plastico +=cantidad?:1
        }
    }
}
