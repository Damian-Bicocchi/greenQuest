package com.example.greenquest.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.greenquest.LogroProvider
import com.example.greenquest.adapters.AdapterLogro
import com.example.greenquest.database.user.User
import com.example.greenquest.databinding.FragmentMiPerfilBinding
import com.example.greenquest.repository.UsuarioRepository
import kotlinx.coroutines.launch


class MiPerfilFragment : Fragment() {

    private lateinit var usuario : User
    private lateinit var binding: FragmentMiPerfilBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

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
            val action = MiPerfilFragmentDirections.actionMiPerfilToConfiguracionFragment()
            findNavController().navigate(action)
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
}