package com.mas.quranwords.ui

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mas.quranwords.R
import com.mas.quranwords.databinding.FragmentListBinding
import com.mas.quranwords.domain.ItemTypeResolver
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

        ItemTypeResolver.execute(itemType, viewModel)
    }

    private fun setupRecyclerView() {

        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        val divider = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(requireContext(), R.drawable.recycler_divider)?.let {
            divider.setDrawable(it)
        }
        binding.recyclerView.addItemDecoration(divider)

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