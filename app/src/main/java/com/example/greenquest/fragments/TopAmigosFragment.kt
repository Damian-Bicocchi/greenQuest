package com.example.greenquest.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.greenquest.AdapterUsuarios
import com.example.greenquest.R
import com.example.greenquest.User

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FifthFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TopAmigosFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val usuarios = listOf(
        User(userName = "Amigo1", puntos = 150, imagen = "none", uid = 1, password = null, accessToken = null, refreshToken = null),
        User(userName = "Amigo2", puntos = 150, imagen = "none", uid = 2, password = null, accessToken = null, refreshToken = null),
        User(userName = "Amigo3", puntos = 150, imagen = "none", uid = 3, password = null, accessToken = null, refreshToken = null),
        User(userName = "Amigo4", puntos = 150, imagen = "none", uid = 4, password = null, accessToken = null, refreshToken = null),
        User(userName = "Amigo5", puntos = 150, imagen = "none", uid = 5, password = null, accessToken = null, refreshToken = null),
        User(userName = "Amigo6", puntos = 150, imagen = "none", uid = 6, password = null, accessToken = null, refreshToken = null),
        User(userName = "Amigo7", puntos = 150, imagen = "none", uid = 7, password = null, accessToken = null, refreshToken = null),
        User(userName = "Amigo8", puntos = 150, imagen = "none", uid = 8, password = null, accessToken = null, refreshToken = null),
        User(userName = "Amigo9", puntos = 150, imagen = "none", uid = 9, password = null, accessToken = null, refreshToken = null),
        User(userName = "Amigo10", puntos = 150, imagen = "none", uid = 10, password = null, accessToken = null, refreshToken = null),
        User(userName = "Amigo111", puntos = 150, imagen = "none", uid = 11, password = null, accessToken = null, refreshToken = null),

    )



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_top_amigos, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerTopAmigos)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = AdapterUsuarios(usuarios)
    }


}