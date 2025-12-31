package com.example.greenquest.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.greenquest.R
import com.example.greenquest.viewmodel.TriviaViewModel


class TriviaFragment : Fragment() {

    private lateinit var triviaViewModel: TriviaViewModel
    private var selectedOptionId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        triviaViewModel = ViewModelProvider(this)[TriviaViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_trivia,
            container,
            false
        )
    }


}