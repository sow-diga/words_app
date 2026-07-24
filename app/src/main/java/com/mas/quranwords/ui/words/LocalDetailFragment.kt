package com.mas.quranwords.ui.words


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
import com.mas.quranwords.audio.AudioMode
import com.mas.quranwords.audio.PlaybackSpeed
import com.mas.quranwords.data.QuranRepository
import com.mas.quranwords.data.db.WordRecord
import com.mas.quranwords.data.repository.LocalWordRepositoryProvider
import com.mas.quranwords.databinding.FragmentLocalDetailBinding
import com.mas.quranwords.navigation.NavigationArgs
import com.mas.quranwords.player.AudioPlayer
import com.mas.quranwords.ui.mapper.renderState
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

    private var currentWord: WordRecord? = null
    private var selectedPlaybackSpeed = PlaybackSpeed.NORMAL
    private var audioMode = AudioMode.WORD
    private var editHide = false
    private val progressTracker = TaskProgressTracker(maxListen = 5, maxRepeat = 10)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLocalDetailBinding.bind(view)

        loadWord()
        setupButtons()
        setupTracker()
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
            detailEnglishTextView.text = word.english
            detailCommentTextView.text = word.comment
            val surahName = QuranRepository.getSurahName(word.surahNumber)
            detailSurahTextView.text = "${surahName}: ${word.ayahNumber}"
            detailAyahTextView.text = QuranRepository.getVerseText(word.surahNumber, word.ayahNumber)
        }
    }

    private fun setupButtons() {
        binding.editButton.setOnClickListener {
            findNavController()
                .navigate(
                    R.id.editWordFragment,
                    Bundle().apply {
                        putLong(NavigationArgs.WORD_ID, wordId)
                    }
                )
        }

        binding.deleteButton.setOnClickListener {
            showDeleteDialog()
        }

        binding.audioModeGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener
            audioMode = when (checkedId) {
                R.id.wordAudioButton -> AudioMode.WORD
                R.id.suwaidButton -> AudioMode.SUWAID
                R.id.husaryButton -> AudioMode.HUSARY
                else -> AudioMode.WORD
            }
        }

        binding.playAudioButton.setOnClickListener {
            currentWord?.let {
                AudioPlayer.play(UrlBuilder.buildLocalAudio(audioMode, it))
            }
        }

        binding.navigationButton.setOnClickListener {
            editLayoutControl()
        }
    }

    private fun setupTracker() {
        binding.taskProgressLayout.renderState(progressTracker.state, animate = false)
        binding.root.setOnClickListener {
            val wasPhase1Complete = progressTracker.state.isPhase1Complete
            val newState = progressTracker.registerTap()
            binding.taskProgressLayout.renderState(newState, animate = true)

            if (!wasPhase1Complete && newState.isPhase1Complete) {
                triggerSuccessVibration(requireContext())
            }

            if (newState.isFullyComplete) {
                AudioPlayer.playAssetAudio(requireContext(), "success_chime.mp3")
                triggerSuccessVibration(requireContext())
            }
        }
    }

    private fun initUi() {
        binding.playLoop.setOnCheckedChangeListener { _, isChecked ->
            AudioPlayer.setSingleLoop(isChecked)
        }

        initializePlaybackSpeed()
        editLayoutControl()
    }

    private fun initializePlaybackSpeed() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, PlaybackSpeed.values())
        binding.playbackSpeedDropdown.setAdapter(adapter)
        binding.playbackSpeedDropdown.setText(selectedPlaybackSpeed.label, false)
        binding.playbackSpeedDropdown.setOnItemClickListener { parent, _, position, _ ->
            selectedPlaybackSpeed = parent.getItemAtPosition(position) as PlaybackSpeed
            AudioPlayer.setSpeed(selectedPlaybackSpeed.value)
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
            deleteButton.isVisible = editHide
            editButton.isVisible = editHide
            if (editHide) {
                navigationButton.setBackgroundResource(android.R.drawable.ic_menu_send)
            } else {
                navigationButton.setBackgroundResource(android.R.drawable.ic_menu_view)
            }
        }
        editHide = !editHide
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}