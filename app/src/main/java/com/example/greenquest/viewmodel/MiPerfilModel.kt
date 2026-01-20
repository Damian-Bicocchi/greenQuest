package com.example.greenquest.viewmodel

import androidx.lifecycle.ViewModel
import com.example.greenquest.Provider.LogroProvider
import com.example.greenquest.adapters.AdapterLogro
import com.example.greenquest.apiParameters.TipoResiduo
import com.example.greenquest.database.user.User

class MiPerfilModel : ViewModel() {

    suspend fun chequearYActualizarLogros(usuario: User){
        LogroProvider.chequearYActualizarLogros(TipoResiduo.PAPEL,usuario.cant_papeles)
        LogroProvider.chequearYActualizarLogros(TipoResiduo.CARTON,usuario.cant_cartones)
        LogroProvider.chequearYActualizarLogros(TipoResiduo.METAL,usuario.cant_metal)
        LogroProvider.chequearYActualizarLogros(TipoResiduo.PLASTICO,usuario.cant_plastico)
        LogroProvider.chequearYActualizarLogros(TipoResiduo.VIDRIO,usuario.cant_vidrio)

    }



}