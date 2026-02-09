package com.example.greenquest.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.greenquest.Provider.LogroProvider
import com.example.greenquest.R
import com.example.greenquest.adapters.AdapterLogro
import com.example.greenquest.database.user.User
import com.example.greenquest.databinding.FragmentMiPerfilBinding
import com.example.greenquest.dialog.FotoDialogFragment
import com.example.greenquest.repository.UsuarioRepository
import com.example.greenquest.viewmodel.MiPerfilModel
import kotlinx.coroutines.launch


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MiPerfilFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private lateinit var usuario : User

    private var subrayarConseguidos = false
    private var subrayarFaltantes = false

    private val miPerfilModel : MiPerfilModel by viewModels()

    private lateinit var binding: FragmentMiPerfilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMiPerfilBinding.inflate(inflater, container, false)
        val recyclerView = binding.logrosRecyclerview


        lifecycleScope.launch {
            usuario = UsuarioRepository.obtenerUsuarioLocal()!!
            binding.usernameEditperfil.setText(usuario.userName.toString())
            binding.usernameEditperfil.setEnabled(false)
            binding.descripcionEditperfil.setEnabled(false)
            binding.descripcionEditperfil.setText(usuario.descripcion ?: "¡Hola! Soy nuevo en GreenQuest.")
            binding.imagenDePerfil.setImageResource(usuario.imagen?:R.drawable.outline_person_24)
            miPerfilModel.chequearYActualizarLogros(usuario)
            recyclerView.adapter = AdapterLogro(LogroProvider.obtenerLogrosObtenidosPrimero())
            //Log.d("CANTIDAD DE RESIUDOS", "Papel: ${usuario.cant_papeles}, Carton: ${usuario.cant_cartones}, Metal: ${usuario.cant_metal}, Plastico: ${usuario.cant_plastico}, Vidrio: ${usuario.cant_vidrio}")
        }

        binding.imagenDePerfil.setOnClickListener {
            FotoDialogFragment().show(parentFragmentManager, "FotoDialogFragment")
        }


        binding.configuracionButton.setOnClickListener {
            findNavController().navigate(R.id.configuracionFragment)

        }

        binding.logrosConseguidosButton.setOnClickListener {
            subrayarConseguidos = !subrayarConseguidos
            subrayarFaltantes = false
            if(!subrayarConseguidos && !subrayarFaltantes){
                recyclerView.adapter = AdapterLogro(LogroProvider.obtenerLogrosObtenidosPrimero())
            }else {
                val listaLogrosObtenidos = LogroProvider.logrosObtenidos()
                recyclerView.adapter = AdapterLogro(listaLogrosObtenidos)
            }
        }

        binding.logrosFaltantesButton.setOnClickListener{
            subrayarFaltantes = !subrayarFaltantes
            subrayarConseguidos = false
            if(!subrayarConseguidos && !subrayarFaltantes){
                recyclerView.adapter = AdapterLogro(LogroProvider.obtenerLogrosObtenidosPrimero())
            }else {
                recyclerView.adapter = AdapterLogro(LogroProvider.logrosNoObtenidos())
            }
        }




        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener("foto_perfil_result") { _, bundle ->
            val resId = bundle.getInt("imagenResId")
            binding.imagenDePerfil.setImageResource(resId) // 👈 se actualiza al toque
        }    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MiPerfilFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            subrayarFaltantes = false
            subrayarConseguidos = false
            usuario = UsuarioRepository.obtenerUsuarioLocal()!!
            binding.usernameEditperfil.setText(usuario.userName.toString())
            binding.usernameEditperfil.setEnabled(false)
            binding.descripcionEditperfil.setEnabled(false)
            binding.imagenDePerfil.setImageResource(usuario.imagen?:R.drawable.outline_person_24)
            miPerfilModel.chequearYActualizarLogros(usuario)
            binding.logrosRecyclerview.adapter = AdapterLogro(LogroProvider.obtenerLogrosObtenidosPrimero())
        }
    }
}