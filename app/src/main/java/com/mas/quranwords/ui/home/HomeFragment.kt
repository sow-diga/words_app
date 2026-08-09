package com.mas.quranwords.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.mas.quranwords.R
import com.mas.quranwords.data.db.AppDatabase
import com.mas.quranwords.data.repository.LocalWordRepository
import com.mas.quranwords.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        val dao = AppDatabase.getInstance(requireContext()).wordDao()
        val repository = LocalWordRepository(dao)
        HomeViewModelFactory(repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        setupNavigation()
        observeWordCount()
    }

    private fun setupNavigation() {
        binding.wordsCard.setOnClickListener {
            findNavController().navigate(R.id.wordListFragment)
        }

        binding.numbersCard.setOnClickListener {
            findNavController().navigate(R.id.numbersFragment)
        }

        // Coming soon
        binding.reviewCard.isEnabled = false
    }

    private fun observeWordCount() {
        viewLifecycleOwner.apply {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.wordCount.collect { count ->
                        binding.wordsDescriptionText.text =
                            resources.getQuantityString(R.plurals.home_word_count, count, count)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}