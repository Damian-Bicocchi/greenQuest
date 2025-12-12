package com.example.greenquest.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.greenquest.GreenQuestApp
import com.example.greenquest.R
import com.example.greenquest.TokenDataStoreProvider
import com.example.greenquest.databinding.FragmentTopGlobalBinding
import com.example.greenquest.ui.iniciar_sesion
import com.example.greenquest.repository.UsuarioRepository
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FirstFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TopGlobalFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentTopGlobalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            val user = UsuarioRepository.obtenerUsuarioLocal()
            Log.d("TopGlobalFragment", "Usuario obtenido: $user")
            binding.textGlobal.text =
                if (user != null) {
                    "${user.userName} con id ${user.uid}"
                } else {
                    "Invitado con id N/A"
                }
        }
            binding.cerrarSesion.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    val usuario = UsuarioRepository.obtenerUsuarioLocal()
                    val response = UsuarioRepository.logout()

                    if(response.isSuccess){
                        TokenDataStoreProvider.get().clearAllTokens()
                        UsuarioRepository.eliminarUsuarioLocal(usuario!!)
                        Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(requireContext(), iniciar_sesion::class.java))
                    } else {
                        Toast.makeText(requireContext(), "Error al cerrar sesión ${response.toString()}", Toast.LENGTH_SHORT).show()
                    }


                }
            }

        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTopGlobalBinding.inflate(inflater, container, false)

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FirstFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TopGlobalFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}