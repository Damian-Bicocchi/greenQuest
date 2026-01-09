package com.example.greenquest.fragments

import android.content.Intent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.greenquest.R
import com.example.greenquest.ui.iniciar_sesion
import com.example.greenquest.viewmodel.ConfiguracionViewModel
import kotlinx.coroutines.launch
import kotlin.getValue

class ConfiguracionFragment : Fragment() {

    lateinit var binding : com.example.greenquest.databinding.FragmentConfiguracionBinding
    private val viewModel: ConfiguracionViewModel by viewModels()


    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: android.os.Bundle?
    ): android.view.View? {
        binding = com.example.greenquest.databinding.FragmentConfiguracionBinding.inflate(layoutInflater)



        binding.cerrarSesionButton.setOnClickListener{
            lifecycleScope.launch {
                val res = viewModel.cerrarSesion()
                if(res.isFailure){
                    Toast.makeText(context, "Error al cerrar sesión", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(context, iniciar_sesion::class.java))
                }
            }
        }
        return binding.root
    }



}