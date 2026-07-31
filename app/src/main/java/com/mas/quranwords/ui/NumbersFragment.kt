package com.mas.quranwords.ui

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.view.View
import androidx.fragment.app.Fragment
import com.mas.quranwords.R
import com.mas.quranwords.databinding.FragmentNumbersBinding
import com.mas.quranwords.util.Preferences
import java.util.*
import kotlin.random.Random


class NumbersFragment : Fragment(R.layout.fragment_numbers) , TextToSpeech.OnInitListener {

    private lateinit var binding: FragmentNumbersBinding
    private lateinit var tts: TextToSpeech

    private var currentNum: Int? = null
    private var score = 0
    private var streak = 0

    enum class LearningMode(val min: Int, val max: Int) {
        EASY(1, 100),
        MEDIUM(1, 1000),
        HARD(1, 10000)
    }
    private var mode = LearningMode.MEDIUM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNumbersBinding.bind(view)

        restoreLearningMode()

        tts = TextToSpeech(requireActivity(), this)

        setupModeSelector()
        setupButtons()
    }

    private fun restoreLearningMode() {
        val savedMode = Preferences.getLearningMode(requireContext(), LearningMode.MEDIUM.name)
        mode = LearningMode.valueOf(savedMode)
        when (mode) {
            LearningMode.EASY -> binding.easyMode.isChecked = true
            LearningMode.MEDIUM -> binding.mediumMode.isChecked = true
            LearningMode.HARD -> binding.hardMode.isChecked = true
        }
    }

    private fun setupModeSelector() {
        binding.modeGroup.setOnCheckedChangeListener { _, checkedId ->
            mode = when (checkedId) {
                binding.easyMode.id -> LearningMode.EASY
                binding.mediumMode.id -> LearningMode.MEDIUM
                binding.hardMode.id -> LearningMode.HARD
                else -> LearningMode.MEDIUM
            }

            Preferences.saveLearningMode(requireContext(), mode.name)
        }
    }

    private fun setupButtons() {

        binding.newBtn.setOnClickListener {
            currentNum = Random.nextInt(mode.min, mode.max + 1)
            binding.answerInput.setText("")
            binding.feedback.visibility = View.GONE

            currentNum?.let {
                speak(it.toString())
            }
        }

        binding.repeatBtn.setOnClickListener {
            currentNum?.let {
                speak(it.toString())
            }
        }

        binding.checkBtn.setOnClickListener {
            val userGuess = binding.answerInput.text.toString().toIntOrNull()
            binding.feedback.visibility = View.VISIBLE

            if (userGuess != null && userGuess == currentNum) {
                score++
                streak++

                binding.feedback.text = "✓ Correct!"
                binding.feedback.setTextColor(requireActivity().getColor(android.R.color.holo_green_dark))
                speak("أحسنتْ", "CORRECT")
            } else {
                streak = 0
                binding.feedback.text = "✗ Incorrect. Try again!"
                binding.feedback.setTextColor(requireActivity().getColor(android.R.color.holo_red_dark))
                binding.answerInput.setText("")
                speak("حاول مرة أخرى", "INCORRECT")
            }

            updateScoreUI()
        }

        binding.answerInput.setOnEditorActionListener { _, actionId, _ ->

            val isDone = actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE

            if (isDone) {
                binding.checkBtn.performClick()
                true
            } else {
                false
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale("ar", "SA")
        }

        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {

            override fun onStart(utteranceId: String?) {}

            override fun onDone(utteranceId: String?) {
                if (utteranceId == "CORRECT") {
                    requireActivity().runOnUiThread {
                        binding.newBtn.performClick()
                    }
                } else if (utteranceId == "INCORRECT") {
                    requireActivity().runOnUiThread {
                        binding.repeatBtn.performClick()
                    }
                }
            }

            override fun onError(utteranceId: String?) {}
        })
    }

    private fun speak(text: String, utteranceId: String = UUID.randomUUID().toString()) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }

    override fun onDestroy() {
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }

    private fun updateScoreUI() {
        binding.scoreText.text = "Score: $score | Streak: $streak"
    }

}