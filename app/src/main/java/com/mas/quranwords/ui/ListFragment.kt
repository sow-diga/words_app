package com.mas.quranwords.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mas.quranwords.R
import com.mas.quranwords.databinding.FragmentListBinding
import com.mas.quranwords.models.ItemType
import com.mas.quranwords.ui.adapter.WordAdapter
import com.mas.quranwords.util.EXTRA_TYPE
import com.mas.quranwords.util.EXTRA_WORD

class ListFragment : Fragment(R.layout.fragment_list) {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: WordAdapter
    private lateinit var itemType: ItemType

    private val viewModel: WordViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentListBinding.bind(view)

        setupRecyclerView()
        observeData()

        itemType = ItemType.valueOf(
            requireActivity()
                .intent
                .getStringExtra(EXTRA_TYPE)
                ?: ItemType.WORD.name
        )

        when (itemType) {
            ItemType.WORD -> viewModel.fetchWords()
            ItemType.MEMORIZE -> viewModel.fetchMemorizeWords()
            ItemType.MISTAKE -> viewModel.fetchMistakes()
        }
    }

    private fun setupRecyclerView() {

        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        adapter = WordAdapter { word ->

            val bundle = Bundle().apply {
                putParcelable(EXTRA_WORD, word)
                putString(EXTRA_TYPE, itemType.name)
            }

            findNavController().navigate(
                R.id.detailFragment,
                bundle
            )
        }

        binding.recyclerView.adapter = adapter
    }

    private fun observeData() {
        viewModel.words.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}