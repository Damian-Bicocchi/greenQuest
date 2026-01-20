package com.example.greenquest.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.greenquest.R
import com.example.greenquest.database.user.User
import com.example.greenquest.repository.UsuarioRepository
import kotlinx.coroutines.launch
import com.example.greenquest.databinding.*

class TiendaFragment : Fragment() {
    lateinit var usuario: User

    lateinit var binding : FragmentTiendaBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTiendaBinding.inflate(inflater, container, false)
        lifecycleScope.launch {
            usuario = UsuarioRepository.obtenerUsuarioLocal()!!
            binding.textviewCantMonedas.text = usuario.monedas.toString()
        }
        return binding.root
    }

}