package com.example.greenquest.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.R
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.greenquest.adapters.AdapterImagen
import com.example.greenquest.databinding.DialogoLayoutBinding
import com.example.greenquest.repository.UsuarioRepository
import com.example.greenquest.viewmodel.TiendaViewModel
import kotlinx.coroutines.launch

class FotoDialogFragment : DialogFragment() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var binding : DialogoLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogoLayoutBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerViewDialogo
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        lifecycleScope.launch {
            val usuario = UsuarioRepository.obtenerUsuarioLocal()!!
            binding.imagenActualCambiarImagen.setImageResource(usuario.imagen ?: com.example.greenquest.R.drawable.outline_person_24 )


            recyclerView.adapter = AdapterImagen(
                TiendaViewModel().articulosAdquiridosIds(usuario)
            )
        }

        binding.buttonCancelar.setOnClickListener {
            dialog?.dismiss()
        }

        binding.buttonGuardar.setOnClickListener {
            lifecycleScope.launch {
                val usuario = UsuarioRepository.obtenerUsuarioLocal()!!
                val nuevaImagenId = binding.imagenActualCambiarImagen.tag as Int
                usuario.imagen = nuevaImagenId
                UsuarioRepository.actualizarUsuarioLocal(usuario)
                setFragmentResult(
                    "foto_perfil_result",
                    Bundle().apply { putInt("imagenResId", nuevaImagenId) }
                )
                val miPerfil : ImageView = requireActivity().findViewById(com.example.greenquest.R.id.miPerfil)
                miPerfil.setImageResource(nuevaImagenId)
                dialog?.dismiss()
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}