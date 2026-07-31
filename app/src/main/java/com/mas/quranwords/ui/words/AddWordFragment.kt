package com.mas.quranwords.ui.words


import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mas.quranwords.R
import com.mas.quranwords.data.QuranRepository
import com.mas.quranwords.data.db.AppDatabase
import com.mas.quranwords.data.repository.LocalWordRepository
import com.mas.quranwords.databinding.FragmentAddWordBinding
import kotlinx.coroutines.launch


class AddWordFragment : Fragment(R.layout.fragment_add_word) {
    private var _binding: FragmentAddWordBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: LocalWordViewModel by viewModels {
        LocalWordViewModelFactory(
            LocalWordRepository(AppDatabase.getInstance(requireContext()).wordDao())
        )
    }
    private lateinit var form: WordFormHelper
    private var currentVerse:String? = ""
    private var currentWords = emptyList<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddWordBinding.bind(view)

        form = WordFormHelper(requireContext(), binding)
        form.setupDropdowns()
        setupSaveButton()
    }

    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener {
            val word = binding.wordInput.text.toString().trim()
            val english = binding.englishInput.text.toString().trim()
            if(word.isEmpty()) {
                binding.wordInput.error = "Required"
                return@setOnClickListener
            }

            val record = form.buildRecord()
            viewModel.insert(record)

            findNavController().popBackStack()
        }

        binding.loadAyahButton.setOnClickListener {
            val surah = binding.surahInput.text.toString().toIntOrNull()
            val ayah = binding.ayahInput.text.toString().toIntOrNull()
            if (surah == null || ayah == null)
                return@setOnClickListener

            lifecycleScope.launch {
                currentVerse = QuranRepository.getVerseText(surah, ayah)
                currentVerse?.let {
                    currentWords = it.split(" ")
                }
                showVerse()
            }
        }
    }

    private fun showVerse() {
        val spannable = SpannableString(currentVerse)
        var start = 0

        currentWords.forEachIndexed { index, word ->
            val end = start + word.length
            spannable.setSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        selectWord(index, word)
                    }
                },
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            start = end + 1
        }

        binding.ayahText.text = spannable
        binding.ayahText.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun selectWord(position: Int, word: String) {
        binding.wordInput.setText(word)
        binding.positionInput.setText((position + 1).toString())
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}