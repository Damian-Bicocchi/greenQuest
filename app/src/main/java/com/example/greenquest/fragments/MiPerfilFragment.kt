package com.example.greenquest.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.greenquest.adapters.AdapterLogro
import com.example.greenquest.Logro
import com.example.greenquest.LogroProvider
import com.example.greenquest.R
import com.example.greenquest.database.user.User
import com.example.greenquest.databinding.FragmentMiPerfilBinding
import com.example.greenquest.repository.UsuarioRepository

import kotlinx.coroutines.launch


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MiPerfilFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private lateinit var usuario : User
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

        val logros = LogroProvider.logros
        recyclerView.adapter = AdapterLogro(logros)
        lifecycleScope.launch {
            usuario = UsuarioRepository.obtenerUsuarioLocal()!!
            binding.usernameEditperfil.setText(usuario.userName.toString())
            binding.usernameEditperfil.setEnabled(false)
            binding.descripcionEditperfil.setEnabled(false)

        }

        binding.configuracionButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container, ConfiguracionFragment())
                .commit()


        }

        binding.logrosConseguidosTextview.setOnClickListener {
            Toast.makeText(context, "Has conseguido ${logros.size} logros!", Toast.LENGTH_SHORT).show()
        }

        binding.logrosFaltantesTextview.setOnClickListener{
            Toast.makeText(context, "Te faltan ${10 - logros.size} logros para ser un experto!", Toast.LENGTH_SHORT).show()
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


}