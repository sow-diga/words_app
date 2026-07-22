package com.mas.quranwords.ui.words

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import com.mas.quranwords.R
import com.mas.quranwords.data.db.AppDatabase
import com.mas.quranwords.data.mapper.toWordItem
import com.mas.quranwords.data.repository.LocalWordRepository
import com.mas.quranwords.databinding.FragmentWordListBinding


class WordListFragment : Fragment(R.layout.fragment_word_list) {
    private var _binding: FragmentWordListBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: LocalWordViewModel by viewModels {
        val dao = AppDatabase.getInstance(requireContext()).wordDao()
        val repository = LocalWordRepository(dao)
        LocalWordViewModelFactory(repository)
    }

    private lateinit var adapter: LocalWordAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentWordListBinding.bind(view)
        setupRecyclerView()
        observeWords()
        binding.addWordFab.setOnClickListener {

            findNavController().navigate(
                R.id.addWordFragment
            )
        }
    }

    private fun setupRecyclerView() {
        adapter = LocalWordAdapter { word ->
            findNavController().navigate(
                    R.id.localDetailFragment,
                    bundleOf(
                        "wordId" to word.id
                    )
                )
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@WordListFragment.adapter
        }
    }

    private fun observeWords() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                viewModel.words.collect { words ->
                    adapter.submitList(words)
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}