package com.mas.quranwords.ui.words


import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

import com.mas.quranwords.R
import com.mas.quranwords.data.QuranRepository
import com.mas.quranwords.data.db.AppDatabase
import com.mas.quranwords.data.db.WordCategory
import com.mas.quranwords.data.db.WordLevel
import com.mas.quranwords.data.db.WordRecord
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.lifecycle.lifecycleScope
import com.mas.quranwords.data.repository.LocalWordRepository
import com.mas.quranwords.databinding.FragmentAddWordBinding
import kotlinx.coroutines.launch


class AddWordFragment :
    Fragment(R.layout.fragment_add_word) {


    private var _binding: FragmentAddWordBinding? = null

    private val binding
        get() = _binding!!



    private val viewModel: LocalWordViewModel by viewModels {
        LocalWordViewModelFactory(
            LocalWordRepository(
                AppDatabase.getInstance(
                    requireContext()
                ).wordDao()
            )
        )
    }


    private var category =
        WordCategory.WORD


    private var level =
        WordLevel.MEDIUM

    private var currentVerse:String? = ""

    private var currentWords = emptyList<String>()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(
            view,
            savedInstanceState
        )


        _binding =
            FragmentAddWordBinding.bind(
                view
            )


        setupDropdowns()

        setupSaveButton()

    }



    private fun setupDropdowns() {


        val categories =
            listOf(
                WordCategory.WORD,
                WordCategory.MEMORIZE,
                WordCategory.MISTAKE,
                WordCategory.AYAH
            )


        binding.categoryDropdown.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                categories
            )
        )


        binding.categoryDropdown.setText(
            category,
            false
        )


        binding.categoryDropdown.setOnItemClickListener { _,_,position,_ ->

            category =
                categories[position]

        }



        val levels =
            listOf(
                WordLevel.EASY,
                WordLevel.MEDIUM,
                WordLevel.HARD
            )


        binding.levelDropdown.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                levels
            )
        )


        binding.levelDropdown.setText(
            level,
            false
        )


        binding.levelDropdown.setOnItemClickListener { _,_,position,_ ->

            level =
                levels[position]

        }
    }



    private fun setupSaveButton() {


        binding.saveButton.setOnClickListener {


            val word =
                binding.wordInput.text
                    .toString()
                    .trim()


            val english =
                binding.englishInput.text
                    .toString()
                    .trim()


            if(word.isEmpty()) {

                binding.wordInput.error =
                    "Required"

                return@setOnClickListener
            }



            val record =
                WordRecord(

                    word = word,

                    english = english,

                    surahNumber =
                    binding.surahInput.text
                        .toString()
                        .toIntOrNull()
                        ?: 0,


                    ayahNumber =
                    binding.ayahInput.text
                        .toString()
                        .toIntOrNull()
                        ?: 0,


                    wordPosition =
                    binding.positionInput.text
                        .toString()
                        .toIntOrNull()
                        ?: 0,


                    category = category,

                    level = level,


                    comment =
                    binding.commentInput.text
                        .toString()
                )


            viewModel.insert(
                record
            )


            findNavController()
                .popBackStack()
        }

        binding.loadAyahButton.setOnClickListener {

            val surah =
                binding.surahInput.text.toString().toIntOrNull()

            val ayah =
                binding.ayahInput.text.toString().toIntOrNull()

            if (surah == null || ayah == null)
                return@setOnClickListener

            lifecycleScope.launch {

                currentVerse =
                    QuranRepository.getVerseText(surah, ayah)

                currentVerse?.let {
                    currentWords =
                        it.split(" ")
                }


                showVerse()
            }
        }
    }

    private fun showVerse() {

        val spannable =
            SpannableString(currentVerse)

        var start = 0

        currentWords.forEachIndexed { index, word ->

            val end = start + word.length

            spannable.setSpan(

                object : ClickableSpan() {

                    override fun onClick(widget: View) {

                        selectWord(
                            index,
                            word
                        )

                    }

                },

                start,
                end,

                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE

            )

            start = end + 1
        }

        binding.ayahText.text = spannable

        binding.ayahText.movementMethod =
            LinkMovementMethod.getInstance()
    }

    private fun selectWord(
        position: Int,
        word: String
    ) {

        binding.wordInput.setText(word)

        binding.positionInput.setText(
            (position + 1).toString()
        )

    }


    override fun onDestroyView() {

        _binding = null

        super.onDestroyView()
    }
}