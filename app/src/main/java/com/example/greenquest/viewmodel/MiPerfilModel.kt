package com.example.greenquest.viewmodel

import androidx.lifecycle.ViewModel
import com.example.greenquest.database.tienda.Articulo
import com.example.greenquest.provider.ArticulosProvider
import com.example.greenquest.provider.LogroProvider
import com.example.greenquest.apiParameters.TipoResiduo
import com.example.greenquest.database.user.User
import com.example.greenquest.repository.TiendaAdquiridosRepository
import com.example.greenquest.repository.UsuarioRepository

class MiPerfilModel : ViewModel() {

    fun chequearYActualizarLogros(usuario: User){
        LogroProvider.chequearYActualizarLogros(TipoResiduo.PAPEL,usuario.cantPapeles)
        LogroProvider.chequearYActualizarLogros(TipoResiduo.CARTON,usuario.cantCartones)
        LogroProvider.chequearYActualizarLogros(TipoResiduo.METAL,usuario.cantMetal)
        LogroProvider.chequearYActualizarLogros(TipoResiduo.PLASTICO,usuario.cantPlastico)
        LogroProvider.chequearYActualizarLogros(TipoResiduo.VIDRIO,usuario.cantVidrio)

    }

    suspend fun articulosAdquiridosIds(): List<Articulo> {
        val usuario = UsuarioRepository.obtenerIdUsuarioActual()
        val listaArticulosAdquiridos = TiendaAdquiridosRepository.obtenerArticulosAdquiridosUsuario(usuario)
        val listaArticulos = mutableListOf<Articulo>()
        val articulos = ArticulosProvider.articulosTienda
        for(articuloId in listaArticulosAdquiridos) {
            listaArticulos.add(articulos.first { it.id == articuloId })

        }
        return listaArticulos
    }

    suspend fun actualizarFotoDePerfil(imagenId: Int?): Boolean{
        val usuario = UsuarioRepository.obtenerUsuarioLocal()!!
        if(imagenId == null || usuario.imagen == imagenId) return false
        usuario.imagen = imagenId
        UsuarioRepository.actualizarUsuarioLocal(usuario)
        return true
    }
}