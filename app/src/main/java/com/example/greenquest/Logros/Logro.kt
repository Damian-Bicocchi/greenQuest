package com.example.greenquest.Logros

import com.example.greenquest.apiParameters.TipoResiduo

class Logro(
    val imagen : Int,
    val tipoResiduo: TipoResiduo,
    val requisitoCantidad : Int,
    val nombre : String,
    val descripcion : String,
    var obtenido : Boolean
){
     fun chequearCumplimiento(cantidad : Int, tipoResiduo: TipoResiduo){
         if( !obtenido && this.tipoResiduo == tipoResiduo && cantidad >= requisitoCantidad){
             obtenido = true
         }
     }
}