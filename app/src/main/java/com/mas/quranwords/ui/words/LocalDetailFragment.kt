package com.mas.quranwords.ui.words

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mas.quranwords.R
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import com.mas.quranwords.audio.PlaybackSpeed
import com.mas.quranwords.data.QuranRepository
import com.mas.quranwords.data.db.WordRecord
import com.mas.quranwords.data.repository.LocalWordRepositoryProvider
import com.mas.quranwords.databinding.FragmentLocalDetailBinding
import com.mas.quranwords.domain.extensions.showIfNotBlank
import com.mas.quranwords.domain.filter.WordFilter
import com.mas.quranwords.models.PlayerMode
import com.mas.quranwords.navigation.NavigationArgs
import com.mas.quranwords.player.AudioPlayer
import com.mas.quranwords.qari.Reciter
import com.mas.quranwords.qari.Reciters
import com.mas.quranwords.ui.adapter.ReciterAdapter
import com.mas.quranwords.ui.common.OnSwipeTouchListener
import com.mas.quranwords.ui.mapper.renderState
import com.mas.quranwords.util.Preferences
import com.mas.quranwords.util.TaskProgressTracker
import com.mas.quranwords.util.UrlBuilder
import com.mas.quranwords.util.triggerSuccessVibration

class LocalDetailFragment : Fragment(R.layout.fragment_local_detail) {
    private var _binding: FragmentLocalDetailBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: LocalWordViewModel by viewModels {
        LocalWordViewModelFactory(
            LocalWordRepositoryProvider.get(requireContext())
        )
    }

    private val wordId: Long by lazy {
        requireArguments().getLong(NavigationArgs.WORD_ID)
    }

    private fun getWordFilter(): WordFilter {
        return requireArguments().getParcelable(NavigationArgs.WORD_FILTER) ?: WordFilter()
    }

    private var currentWord: WordRecord? = null
    private var selectedPlaybackSpeed = PlaybackSpeed.NORMAL
    private var editHide = false
    private val progressTracker = TaskProgressTracker(maxListen = 5, maxRepeat = 10)
    private var selectedReciter = Reciters.RECITERS_ONLY.first()
    private var playerMode = PlayerMode.PRACTICE

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLocalDetailBinding.bind(view)

        //loadWord()
        observeWord()
        viewModel.loadWord(wordId, getWordFilter())

        setupButtons()
        setupStudyMode()
        setupTracker()
        setupSwipe()
        initUi()
    }

    private fun loadWord() {
        viewModel.getWord(wordId) { word ->
            if(word == null){
                Toast.makeText(requireContext(), "Word not found", Toast.LENGTH_SHORT).show()
                return@getWord
            }

            currentWord = word

            updateUi(word)
        }
    }

    private fun updateUi(word: WordRecord) {
        binding.apply {
            detailWordTextView.text = word.word
            detailEnglishTextView.showIfNotBlank(word.english)
            detailCommentTextView.showIfNotBlank(word.comment)
            val surahName = QuranRepository.getSurahName(word.surahNumber)
            detailSurahTextView.text = "${surahName}: ${word.ayahNumber}"
            detailAyahTextView.text = QuranRepository.getVerseText(word.surahNumber, word.ayahNumber)
        }
    }

    private fun animateWord(direction: Int, action: () -> Unit) {
        val distance =
            if (direction > 0)
                -binding.contentContainer.width * 0.25f
            else
                binding.contentContainer.width * 0.25f

        binding.contentContainer.animate()
            .translationX(distance)
            .alpha(0f)
            .setDuration(120)
            .withEndAction {
                action()
                binding.contentContainer.translationX = -distance
                binding.contentContainer.alpha = 0f
                binding.contentContainer.animate()
                    .translationX(0f)
                    .alpha(1f)
                    .setDuration(120)
                    .start()
            }
            .start()
    }

    private fun setupButtons() {
        binding.bottomBar.setOnEditClick  {
            findNavController()
                .navigate(
                    R.id.editWordFragment,
                    Bundle().apply {
                        putLong(NavigationArgs.WORD_ID, wordId)
                    }
                )
        }

        binding.bottomBar.setOnDeleteClick  {
            showDeleteDialog()
        }

        binding.audioControlLayout.audioModeGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener
            selectedReciter = when (checkedId) {
                R.id.wordAudioButton -> Reciters.WORD_ONLY
                R.id.suwaidButton -> Reciters.AYMAN_SUWAID
                R.id.husaryButton -> Reciters.HUSARY
                else -> Reciters.WORD_ONLY
            }
        }

        binding.audioControlLayout.playAudioButton.setOnClickListener {
            currentWord?.let { word ->
                AudioPlayer.play(UrlBuilder.buildAudio(word, selectedReciter))
            }
        }

        binding.bottomBar.setOnNavigationClick {
            editLayoutControl()
        }

        binding.audioControlLayout.reciterDropdown.setOnItemClickListener { parent, _, position, _ ->
            selectedReciter = parent.getItemAtPosition(position) as Reciter
        }

        binding.nextButton.setOnClickListener {
            goToNextWord()
        }

        binding.previousButton.setOnClickListener {
            goToPreviousWord()
        }
    }

    private fun goToNextWord() {
        animateWord(+1) {
            viewModel.nextWord()
        }
    }

    private fun goToPreviousWord() {
        animateWord(-1) {
            viewModel.previousWord()
        }
    }

    private fun setupStudyMode() {
        with (binding.audioControlLayout) {
            modeGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
                if (!isChecked) return@addOnButtonCheckedListener

                when (checkedId) {
                    R.id.studyModeButton -> {
                        playerMode = PlayerMode.PRACTICE
                        Preferences.savePlayerMode(requireContext(), playerMode)
                        selectDefaultReciter(playerMode)
                        audioModeGroup.isVisible = true
                        reciterDropdownLayout.isVisible = false
                    }

                    R.id.listenModeButton -> {
                        playerMode = PlayerMode.LISTEN
                        Preferences.savePlayerMode(requireContext(), playerMode)
                        selectDefaultReciter(playerMode)
                        audioModeGroup.isVisible = false
                        reciterDropdownLayout.isVisible = true
                    }
                }
            }
        }
    }

    private fun setupTracker() {
        binding.taskProgressLayout.renderState(progressTracker.state, animate = false)
        binding.taskProgressLayout.root.setOnClickListener {
            val wasPhase1Complete = progressTracker.state.isPhase1Complete
            val newState = progressTracker.registerTap()
            binding.taskProgressLayout.renderState(newState, animate = true)

            if (!wasPhase1Complete && newState.isPhase1Complete) {
                triggerSuccessVibration(requireContext())
            }

            if (newState.isFullyComplete) {
                stopLoop()
                triggerSuccessVibration(requireContext())
                AudioPlayer.playAssetAudio(requireContext(), "success_chime.mp3")
            }
        }
    }

    private fun initUi() {
        binding.audioControlLayout.playLoop.setOnCheckedChangeListener { _, isChecked ->
            AudioPlayer.setSingleLoop(isChecked)
        }

        initializeReciters()
        initializePlaybackSpeed()
        editLayoutControl()
        initPlayerMode()
    }

    private fun initializePlaybackSpeed() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, PlaybackSpeed.values())
        with(binding.audioControlLayout.playbackSpeedDropdown) {
            setAdapter(adapter)
            setText(selectedPlaybackSpeed.label, false)
            setOnItemClickListener { parent, _, position, _ ->
                selectedPlaybackSpeed = parent.getItemAtPosition(position) as PlaybackSpeed
                AudioPlayer.setSpeed(selectedPlaybackSpeed.value)
            }
        }
    }

    private fun initializeReciters() {
        val adapter = ReciterAdapter(requireContext(), Reciters.RECITERS_ONLY)
        with(binding.audioControlLayout.reciterDropdown) {
            setAdapter(adapter)
            setText(selectedReciter.name, false)
        }
    }

    private fun showDeleteDialog() {
        val word = currentWord ?: return

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Word")
            .setMessage("Delete \"${word.word}\"?\n\nThis cannot be undone.")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Delete") { _, _ ->
                viewModel.delete(word)
                findNavController().popBackStack()
            }
            .show()
    }

    private fun editLayoutControl() {
        binding.apply {
            binding.bottomBar.setExpanded(editHide)
        }
        editHide = !editHide
    }

    private fun initPlayerMode() {
        playerMode = Preferences.getPlayerMode(requireContext())
        binding.audioControlLayout.modeGroup.check(
            when (playerMode) {
                PlayerMode.PRACTICE -> R.id.studyModeButton
                PlayerMode.LISTEN -> R.id.listenModeButton
            }
        )
        selectDefaultReciter(playerMode)
    }

    private fun selectDefaultReciter(mode: PlayerMode) {
        selectedReciter = when(mode) {
            PlayerMode.PRACTICE -> Reciters.ALL.first()
            PlayerMode.LISTEN -> Reciters.RECITERS_ONLY.first()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun observeWord() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(
                androidx.lifecycle.Lifecycle.State.STARTED
            ) {
                viewModel.currentWord.collect { word ->
                    word ?: return@collect
                    currentWord = word
                    updateUi(word)
                    updateNavigationButtons()
                }
            }
        }
    }

    private fun updateNavigationButtons() {
        binding.previousButton.isEnabled = viewModel.hasPrevious
        binding.nextButton.isEnabled = viewModel.hasNext
        binding.bottomBar.setPosition(viewModel.position, viewModel.total)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupSwipe() {
        binding.contentContainer.setOnTouchListener(
            object : OnSwipeTouchListener(requireContext()) {
                override fun onSwipeLeft() {
                    goToNextWord()
                }

                override fun onSwipeRight() {
                    goToPreviousWord()
                }
            }
        )
    }

    private fun stopLoop() {
        binding.audioControlLayout.playLoop.isChecked = false
        AudioPlayer.stop()
    }
}