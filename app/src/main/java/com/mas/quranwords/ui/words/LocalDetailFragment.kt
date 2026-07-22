package com.mas.quranwords.ui.words


import android.os.Bundle
import android.view.View
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

import com.mas.quranwords.R
import com.mas.quranwords.data.repository.LocalWordRepositoryProvider
import com.mas.quranwords.databinding.FragmentLocalDetailBinding


class LocalDetailFragment :
    Fragment(R.layout.fragment_local_detail) {


    private var _binding:
            FragmentLocalDetailBinding? = null


    private val binding
        get() = _binding!!


    private val viewModel:
            LocalWordViewModel by viewModels {
        LocalWordViewModelFactory(
            LocalWordRepositoryProvider.get(
                requireContext()
            )
        )
    }



    private val wordId: Long by lazy {

        requireArguments()
            .getLong("wordId")

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
            FragmentLocalDetailBinding.bind(view)


        loadWord()


        setupButtons()

    }



    private fun loadWord() {


        viewModel.getWord(
            wordId
        ) { word ->


            if(word == null){

                Toast.makeText(
                    requireContext(),
                    "Word not found",
                    Toast.LENGTH_SHORT
                ).show()

                return@getWord
            }



            binding.detailWordTextView.text =
                word.word


            binding.detailEnglishTextView.text =
                word.english


            binding.detailCommentTextView.text =
                word.comment


            binding.detailAyahTextView.text =
                "Surah ${word.surahNumber}, Ayah ${word.ayahNumber}"

        }
    }



    private fun setupButtons() {


        binding.editButton.setOnClickListener {


            findNavController()
                .navigate(

                    R.id.editWordFragment,

                    Bundle().apply {

                        putLong(
                            "wordId",
                            wordId
                        )
                    }
                )

        }

    }



    override fun onDestroyView() {

        _binding = null

        super.onDestroyView()
    }
}