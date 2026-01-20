package com.example.greenquest.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.greenquest.adapters.AdapterLogro
import com.example.greenquest.Logros.Logro
import com.example.greenquest.Provider.LogroProvider
import com.example.greenquest.R
import com.example.greenquest.apiParameters.TipoResiduo
import com.example.greenquest.database.user.User
import com.example.greenquest.databinding.FragmentMiPerfilBinding
import com.example.greenquest.repository.LogrosRepository
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

    private val miPerfilModel = MiPerfilModel()

    private lateinit var adapterLogro: AdapterLogro

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
            miPerfilModel.chequearYActualizarLogros(usuario)
            recyclerView.adapter = AdapterLogro(LogroProvider.obtenerLogrosObtenidosPrimero())
            Log.d("CANTIDAD DE RESIUDOS", "Papel: ${usuario.cant_papeles}, Carton: ${usuario.cant_cartones}, Metal: ${usuario.cant_metal}, Plastico: ${usuario.cant_plastico}, Vidrio: ${usuario.cant_vidrio}")
        }

        binding.configuracionButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container, ConfiguracionFragment())
                .commit()


        }

        binding.logrosConseguidosTextview.setOnClickListener {
            subrayarConseguidos = !subrayarConseguidos
            subrayarFaltantes = false
            subrayarTexto(binding.logrosFaltantesTextview,subrayarFaltantes)
            subrayarTexto(binding.logrosConseguidosTextview,subrayarConseguidos)
            if(!subrayarConseguidos && !subrayarFaltantes){
                recyclerView.adapter = AdapterLogro(LogroProvider.obtenerLogrosObtenidosPrimero())
            }else {
                val listaLogrosObtenidos = LogroProvider.logrosObtenidos()
                recyclerView.adapter = AdapterLogro(listaLogrosObtenidos)
            }
        }

        binding.logrosFaltantesTextview.setOnClickListener{
            subrayarFaltantes = !subrayarFaltantes
            subrayarConseguidos = false
            if(!subrayarConseguidos && !subrayarFaltantes){
                recyclerView.adapter = AdapterLogro(LogroProvider.obtenerLogrosObtenidosPrimero())
            }else {
                subrayarTexto(binding.logrosConseguidosTextview, subrayarConseguidos)
                subrayarTexto(binding.logrosFaltantesTextview, subrayarFaltantes)
                recyclerView.adapter = AdapterLogro(LogroProvider.logrosNoObtenidos())
            }
        }

        binding.marcosPerfilTextview.setOnClickListener {
            Toast.makeText(context, "¡Próximamente podrás personalizar tu perfil con marcos exclusivos!", Toast.LENGTH_SHORT).show()
        }


        return  binding.root
    }


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


    private fun subrayarTexto(textView: TextView, subrayar: Boolean) {
        val content = textView.text.toString()
        val spannableString = android.text.SpannableString(content)
        if(!subrayar){
            val spans = spannableString.getSpans(
                0,
                content.length,
                android.text.style.UnderlineSpan::class.java
            )
            for (span in spans) {
                spannableString.removeSpan(span)
            }
        }else{
            spannableString.setSpan(
                android.text.style.UnderlineSpan(),
                0,
                content.length,
                android.text.Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
        }
        textView.text = spannableString
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

            miPerfilModel.chequearYActualizarLogros(usuario)
            binding.logrosRecyclerview.adapter = AdapterLogro(LogroProvider.obtenerLogrosObtenidosPrimero())
        }
    }
}