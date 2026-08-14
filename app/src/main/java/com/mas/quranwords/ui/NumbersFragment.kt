package com.mas.quranwords.ui

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.mas.quranwords.R
import com.mas.quranwords.data.settings.SettingsRepository
import com.mas.quranwords.databinding.FragmentNumbersBinding
import com.mas.quranwords.util.Preferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.random.Random


class NumbersFragment : Fragment(R.layout.fragment_numbers), TextToSpeech.OnInitListener {

    private lateinit var binding: FragmentNumbersBinding
    private lateinit var tts: TextToSpeech
    private lateinit var settingsRepository: SettingsRepository
    private var currentNum: Int? = null
    private var score = 0
    private var streak = 0

    private var questionCount = 0
    private var sessionSize = 20
    private var sessionFinished = false

    enum class LearningMode(val min: Int, val max: Int) {
        EASY(1, 100),
        MEDIUM(1, 1000),
        HARD(1, 10000)
    }

    private var mode = LearningMode.MEDIUM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNumbersBinding.bind(view)

        settingsRepository = SettingsRepository(requireContext())

        restoreLearningMode()

        tts = TextToSpeech(requireActivity(), this)

        setupModeSelector()
        setupButtons()

        loadSessionSettings()
    }

    private fun loadSessionSettings() {
        viewLifecycleOwner.lifecycleScope.launch {
            settingsRepository.numbersSessionSize.collect { size ->
                sessionSize = size
            }
        }
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
            if (!sessionFinished) {
                startNewQuestion()
            }
        }

        binding.repeatBtn.setOnClickListener {
            if (!sessionFinished) {
                currentNum?.let {
                    speak(it.toString())
                }
            }
        }

        binding.checkBtn.setOnClickListener {
            checkAnswer()
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

        binding.newSessionBtn.setOnClickListener {
            startNewSession()
        }
    }

    private fun startNewQuestion() {
        currentNum = Random.nextInt(mode.min, mode.max + 1)
        binding.answerInput.setText("")
        binding.feedbackCard.visibility = View.GONE
        currentNum?.let {
            speak(it.toString())
        }
    }

    private fun checkAnswer() {
        if (sessionFinished) {
            return
        }

        val userGuess = binding.answerInput.text.toString().toIntOrNull()
        binding.feedbackCard.visibility = View.VISIBLE

        if (userGuess != null && userGuess == currentNum) {
            score++
            streak++
            questionCount++

            binding.feedback.text = getString(R.string.numbers_correct)
            binding.feedback.setTextColor(requireActivity().getColor(android.R.color.holo_green_dark))

            updateScoreUI()

            if (questionCount >= sessionSize) {
                finishSession()
            } else {
                speak("أحسنتْ", "CORRECT")
            }

        } else {
            streak = 0
            binding.feedback.text = getString(R.string.numbers_incorrect)
            binding.feedback.setTextColor(requireActivity().getColor(android.R.color.holo_red_dark))
            binding.answerInput.setText("")
            updateScoreUI()
            speak("حاول مرة أخرى", "INCORRECT")
        }
    }

    private fun finishSession() {
        sessionFinished = true
        binding.feedback.text = "🎉 Session complete! Score: $score / $sessionSize"
        binding.feedback.setTextColor(requireActivity().getColor(android.R.color.holo_green_dark))

        binding.newBtn.isEnabled = false
        binding.repeatBtn.isEnabled = false
        binding.checkBtn.isEnabled = false
        binding.answerInput.isEnabled = false
        binding.newSessionBtn.visibility = View.VISIBLE
        speak("أحسنتْ")
    }

    private fun startNewSession() {
        score = 0
        streak = 0
        questionCount = 0
        currentNum = null
        sessionFinished = false

        binding.answerInput.setText("")
        binding.feedback.text = ""
        binding.feedbackCard.visibility = View.GONE

        binding.newBtn.isEnabled = true
        binding.repeatBtn.isEnabled = true
        binding.checkBtn.isEnabled = true
        binding.answerInput.isEnabled = true
        binding.newSessionBtn.visibility = View.GONE
        updateScoreUI()

        startNewQuestion()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale("ar", "SA")
        }

        tts.setOnUtteranceProgressListener(
            object : UtteranceProgressListener() {

                override fun onStart(utteranceId: String?) {}

                override fun onDone(utteranceId: String?) {

                    requireActivity().runOnUiThread {
                        when (utteranceId) {
                            "CORRECT" -> {
                                if (!sessionFinished) {
                                    binding.newBtn.performClick()
                                }
                            }
                            "INCORRECT" -> {
                                if (!sessionFinished) {
                                    binding.repeatBtn.performClick()
                                }
                            }
                        }
                    }
                }

                override fun onError(utteranceId: String?) {}
            }
        )
    }

    private fun speak(
        text: String,
        utteranceId: String = java.util.UUID.randomUUID().toString()
    ) {
        tts.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            utteranceId
        )
    }

    private fun updateScoreUI() {
        binding.scoreText.text = "Question: $questionCount / $sessionSize  |  Score: $score  |  Streak: $streak"
    }

    override fun onDestroy() {
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }
}