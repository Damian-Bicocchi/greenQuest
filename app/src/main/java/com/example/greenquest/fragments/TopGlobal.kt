package com.example.greenquest.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.greenquest.R
import com.example.greenquest.apiParameters.TipoResiduo
import com.example.greenquest.adapters.GlobalRankingAdapter
import com.example.greenquest.databinding.FragmentTopGlobalBinding
import com.example.greenquest.viewmodel.TopGlobalViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TopGlobal : Fragment() {
    private var _binding: FragmentTopGlobalBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TopGlobalViewModel by viewModels()
    private lateinit var adapter: GlobalRankingAdapter
    private lateinit var categoryAdapter: ArrayAdapter<TipoResiduo>
    private var selector: TipoResiduo? = null

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
            categoryAdapter = ArrayAdapter(
                it,
                R.layout.dropdown_item,
                TipoResiduo.entries.toTypedArray()
            )
            binding.categorySelectionTextView.setAdapter(categoryAdapter)
            binding.categorySelectionTextView.setText(categoryAdapter.getItem(0).toString(), false)
            binding.categorySelectionTextView.setOnItemClickListener { _, _, position, _ ->
                selector = categoryAdapter.getItem(position)
                viewModel.tipoResiduo = categoryAdapter.getItem(position)
                viewModel.obtenerRanking()
            }
        }

        binding.rankingSelector.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.generalRanking -> {
                        binding.categorySelectionGroup.visibility = View.GONE
                        viewModel.tipoResiduo = null
                        viewModel.obtenerRanking()
                    }

                    R.id.categorizedRanking -> {
                        binding.categorySelectionGroup.visibility = View.VISIBLE
                        viewModel.tipoResiduo = null
                        viewModel.obtenerRanking()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.ranking.collectLatest {
                    adapter.update(it)
                    binding.emptyAlternative.visibility =
                        if (it.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }

        // Refresh ranking when fragment becomes visible
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.obtenerRanking()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.obtenerRanking()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}