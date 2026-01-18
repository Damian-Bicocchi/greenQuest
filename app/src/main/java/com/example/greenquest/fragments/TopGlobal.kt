package com.example.greenquest.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.greenquest.R
import com.example.greenquest.adapters.CategoriaAdapter
import com.example.greenquest.adapters.GlobalRankingAdapter
import com.example.greenquest.databinding.FragmentTopGlobalBinding
import com.example.greenquest.enums.Categoria
import com.example.greenquest.viewmodel.TopGlobalViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TopGlobal : Fragment() {
    private var _binding: FragmentTopGlobalBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TopGlobalViewModel by viewModels()
    private lateinit var adapter: GlobalRankingAdapter
    private lateinit var categoryAdapter: ArrayAdapter<Categoria>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.obtenerRanking()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTopGlobalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = GlobalRankingAdapter(emptyList())
        binding.rankingList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@TopGlobal.adapter
        }

        context?.let {
            categoryAdapter = CategoriaAdapter(requireContext(), R.layout.dropdown_item)
            binding.categorySelectionTextView.setAdapter(categoryAdapter)
            val text = categoryAdapter.getItem(0)?.getString(requireContext()) ?: ""
            binding.categorySelectionTextView.setText(
                text, false
            )
            binding.categorySelectionTextView.setOnItemClickListener { _, _, position, _ ->
                val category = categoryAdapter.getItem(position) ?: return@setOnItemClickListener
                val text = category.getString(requireContext()) ?: ""
                viewModel.tipoResiduo = category.tipoResiduo
                viewModel.obtenerRanking()
                binding.categorySelectionTextView.setText(
                    text, false
                )
            }
        }

        binding.rankingSelector.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.weeklyRanking -> {
                        viewModel.historical = false
                        viewModel.obtenerRanking()
                    }

                    R.id.historicalRanking -> {
                        viewModel.historical = true
                        viewModel.obtenerRanking()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.ranking.collectLatest {
                    adapter.update(it)
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.obtenerRanking()
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}