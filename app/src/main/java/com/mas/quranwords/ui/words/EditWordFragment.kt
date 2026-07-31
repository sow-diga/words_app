package com.mas.quranwords.ui.words

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mas.quranwords.R
import com.mas.quranwords.data.db.WordRecord
import com.mas.quranwords.data.repository.LocalWordRepositoryProvider
import com.mas.quranwords.databinding.FragmentAddWordBinding
import com.mas.quranwords.navigation.NavigationArgs

class EditWordFragment : Fragment(R.layout.fragment_add_word) {

    private var _binding: FragmentAddWordBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: LocalWordViewModel by viewModels {
        LocalWordViewModelFactory(LocalWordRepositoryProvider.get(requireContext()))
    }
    private var existingWord: WordRecord? = null

    private val wordId: Long by lazy {
        requireArguments().getLong(NavigationArgs.WORD_ID)
    }
    private lateinit var form: WordFormHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddWordBinding.bind(view)

        form = WordFormHelper(requireContext(), binding)
        form.setupDropdowns()

        loadWord()

        binding.saveButton.text = "Update Word"
        binding.loadAyahButton.visibility = View.GONE
        binding.ayahText.visibility = View.GONE

        binding.saveButton.setOnClickListener {
            updateWord()
        }
    }

    private fun loadWord() {

        viewModel.getWord(wordId) { word ->
            existingWord = word
            word ?: return@getWord

            form.fill(word)
        }
    }

    private fun updateWord() {
        existingWord?.let {
            viewModel.update(form.buildRecord(it))
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}