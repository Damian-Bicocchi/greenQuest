package com.example.greenquest.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.greenquest.adapters.AdapterArticulo
import com.example.greenquest.databinding.FragmentTiendaBinding
import com.example.greenquest.viewmodel.TiendaViewModel
import kotlinx.coroutines.launch

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

