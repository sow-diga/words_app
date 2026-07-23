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


class EditWordFragment :
    Fragment(R.layout.fragment_add_word) {


    private var _binding:
            FragmentAddWordBinding? = null


    private val binding
        get() = _binding!!


    private val viewModel:
            LocalWordViewModel by viewModels {
        LocalWordViewModelFactory(LocalWordRepositoryProvider.get(requireContext()))
    }

    private var existingWord: WordRecord? = null

    private val wordId: Long by lazy {
        requireArguments().getLong(NavigationArgs.WORD_ID)
    }



    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(
            view,
            savedInstanceState
        )


        _binding =
            FragmentAddWordBinding.bind(view)


        loadWord()


        binding.saveButton.text =
            "Update Word"


        binding.saveButton.setOnClickListener {

            updateWord()

        }
    }



    private fun loadWord() {


        viewModel.getWord(
            wordId
        ) { word ->


            existingWord = word


            word ?: return@getWord


            binding.wordInput.setText(
                word.word
            )


            binding.englishInput.setText(
                word.english
            )


            binding.surahInput.setText(
                word.surahNumber.toString()
            )


            binding.ayahInput.setText(
                word.ayahNumber.toString()
            )


            binding.positionInput.setText(
                word.wordPosition.toString()
            )


            binding.commentInput.setText(
                word.comment
            )


            binding.categoryDropdown.setText(
                word.category,
                false
            )


            binding.levelDropdown.setText(
                word.level,
                false
            )
        }
    }



    private fun updateWord() {


        val old =
            existingWord
                ?: return



        val updated =
            old.copy(

                word =
                binding.wordInput.text
                    .toString(),


                english =
                binding.englishInput.text
                    .toString(),


                surahNumber =
                binding.surahInput.text
                    .toString()
                    .toInt(),


                ayahNumber =
                binding.ayahInput.text
                    .toString()
                    .toInt(),


                wordPosition =
                binding.positionInput.text
                    .toString()
                    .toInt(),


                comment =
                binding.commentInput.text
                    .toString()

                // IMPORTANT:
                // lastReviewed stays
                // reviewCount stays
            )


        viewModel.update(
            updated
        )


        findNavController()
            .popBackStack()
    }



    override fun onDestroyView() {

        _binding = null

        super.onDestroyView()
    }
}