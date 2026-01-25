package com.example.greenquest.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.greenquest.R
import com.example.greenquest.adapters.AdapterArticulo
import com.example.greenquest.adapters.AdapterLogro
import com.example.greenquest.database.user.User
import com.example.greenquest.repository.UsuarioRepository
import kotlinx.coroutines.launch
import com.example.greenquest.databinding.*
import com.example.greenquest.viewmodel.TiendaViewModel

class TiendaFragment : Fragment() {
    private lateinit var usuario: User

    private val tiendaViewModel = TiendaViewModel()

    private lateinit var binding : FragmentTiendaBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTiendaBinding.inflate(inflater, container, false)
        val recyclerView =  binding.MiTiendaRV
        recyclerView.adapter = AdapterArticulo(tiendaViewModel.obtenerArticulosDisponibles())
        lifecycleScope.launch {
            usuario = UsuarioRepository.obtenerUsuarioLocal()!!
            usuario.monedas += 100000 // Para pruebas
            UsuarioRepository.guardarUsuarioLocal(usuario)
            binding.textviewCantMonedas.text = usuario.monedas.toString()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            usuario = UsuarioRepository.obtenerUsuarioLocal()!!
            binding.textviewCantMonedas.text = usuario.monedas.toString()
        }
    }
}