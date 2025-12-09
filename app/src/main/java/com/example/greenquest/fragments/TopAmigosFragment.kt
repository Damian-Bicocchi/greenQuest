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
import com.example.greenquest.Usuario

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
        Usuario("Amigo1", 150,"none"),
        Usuario("Amigo2", 120,"none"),
        Usuario("Amigo3", 100,"none"),
        Usuario("Amigo4", 80,"none"),
        Usuario("Amigo5", 60,"none"),
        Usuario("Amigo6", 50,"none"),
        Usuario("Amigo7", 40,"none"),
        Usuario("Amigo8", 30,"none"),
        Usuario("Amigo9", 20,"none"),
        Usuario("Amigo10", 10,"none")
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