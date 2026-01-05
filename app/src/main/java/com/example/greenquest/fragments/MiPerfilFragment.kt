package com.example.greenquest.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.greenquest.AdapterLogro
import com.example.greenquest.Logro
import com.example.greenquest.R
import com.example.greenquest.User
import com.example.greenquest.databinding.FragmentMiPerfilBinding
import com.example.greenquest.repository.UsuarioRepository
import com.example.greenquest.ui.iniciar_sesion
import com.example.greenquest.viewmodel.MiPerfilViewModel
import kotlinx.coroutines.launch


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MiPerfilFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val viewModel: MiPerfilViewModel by viewModels()

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

        val logros = listOf<Logro>(
            Logro(R.drawable.trash, "Primer Logro", "Has conseguido tu primer logro!"),
            Logro(R.drawable.icon_menu, "Explorador", "Has explorado todas las secciones de la app."),
            Logro(R.drawable.icon_menu, "Amigo de la naturaleza", "Has completado 5 misiones ecológicas." ),
            Logro(R.drawable.trash, "Reciclador", "Has reciclado 10 objetos diferentes."),
            Logro(R.drawable.icon_menu, "Campeón del reciclaje", "Has alcanz)ado 100 puntos de reciclaje.")
        )
        recyclerView.adapter = AdapterLogro(logros)
        lifecycleScope.launch {
            usuario = UsuarioRepository.obtenerUsuarioLocal()!!
            binding.usernameEditperfil.setText(usuario.userName.toString())
            binding.usernameEditperfil.setEnabled(false)
            binding.descripcionEditperfil.setEnabled(false)

        }
        val botonCerrarSesion = binding.cerrarSesionButton
        botonCerrarSesion.setOnClickListener {
            lifecycleScope.launch {
                val res = viewModel.cerrarSesion()
                if(res.isFailure){
                    Toast.makeText(context, "Error al cerrar sesión", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(requireContext(), iniciar_sesion::class.java))
                }
            }
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