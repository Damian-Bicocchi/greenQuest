package com.example.greenquest.repository

import com.example.greenquest.apiParameters.TipoResiduo

object LogrosRepository {
    fun incrementarContadorResiduo(tipoResiduo: TipoResiduo){
        when (tipoResiduo){
            TipoResiduo.BASURA -> {TODO("ACA iria una conexión con el DAO que escriba en un archivo")}
            TipoResiduo.CARTON -> {TODO("ACA iria una conexión con el DAO que escriba en un archivo")}
            TipoResiduo.METAL -> {TODO("ACA iria una conexión con el DAO que escriba en un archivo")}
            TipoResiduo.PAPEL -> {TODO("ACA iria una conexión con el DAO que escriba en un archivo")}
            TipoResiduo.PLASTICO -> {TODO("ACA iria una conexión con el DAO que escriba en un archivo")}
            TipoResiduo.VIDRIO -> {TODO("ACA iria una conexión con el DAO que escriba en un archivo")}
        }
    }
}