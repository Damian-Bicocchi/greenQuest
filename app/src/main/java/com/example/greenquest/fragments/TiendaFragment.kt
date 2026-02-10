package com.example.greenquest.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.greenquest.R
import com.example.greenquest.adapters.AdapterArticulo
import com.example.greenquest.adapters.AdapterLogro
import com.example.greenquest.database.tienda.Articulo
import com.example.greenquest.database.tienda.ArticuloTiendaUI
import com.example.greenquest.database.tienda.CompraArticulo
import com.example.greenquest.database.tienda.TiendaConArticulos
import com.example.greenquest.database.user.User
import com.example.greenquest.repository.UsuarioRepository
import kotlinx.coroutines.launch
import com.example.greenquest.databinding.*
import com.example.greenquest.viewmodel.TiendaViewModel

class TiendaFragment : Fragment() {

    private val tiendaViewModel : TiendaViewModel by viewModels()

    private lateinit var binding : FragmentTiendaBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTiendaBinding.inflate(inflater, container, false)
        val recyclerView =  binding.MiTiendaRV

        lifecycleScope.launch {
            tiendaViewModel.actualizarArticulosAdquiridos()
            binding.textviewCantMonedas.text = tiendaViewModel.actualizarMonedasUsuario()

            val todosLosArticulos : List<Articulo> = tiendaViewModel.obtenerArticulosDisponibles()

            val comprasRealizadasPorUsuario : List<CompraArticulo> = tiendaViewModel
                .obtenerArticulosAdquiridos(
                    UsuarioRepository.obtenerIdUsuarioActual()
                )

            val idsComprados = comprasRealizadasPorUsuario.map { it.articuloId }.toSet()

            val listaParaAdapter = mutableListOf<ArticuloTiendaUI>()
            var comprado: Boolean
            for (elem in todosLosArticulos){
                comprado = idsComprados.contains(elem.id)
                listaParaAdapter.add(
                    ArticuloTiendaUI(articulo = elem, comprado = comprado)
                )
            }

            recyclerView.adapter = AdapterArticulo(tiendaViewModel.obtenerArticulosDisponibles()){
                lifecycleScope.launch {
                    binding.textviewCantMonedas.text = tiendaViewModel.actualizarMonedasUsuario()
                }
            }

        }


        return binding.root
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            lifecycleScope.launch {
                binding.textviewCantMonedas.text = tiendaViewModel.actualizarMonedasUsuario()
            }
        }
    }
}

