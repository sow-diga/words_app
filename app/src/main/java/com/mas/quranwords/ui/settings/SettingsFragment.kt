package com.mas.quranwords.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mas.quranwords.R
import com.mas.quranwords.data.settings.SettingsRepository
import com.mas.quranwords.databinding.FragmentSettingsBinding
import kotlinx.coroutines.launch

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels {
        val repository = SettingsRepository(requireContext())
        SettingsViewModelFactory(repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)

        setupSliders()
        observeSettings()
    }

    private fun setupSliders() {
        with(binding) {
            maxListenSlider.addOnChangeListener { _, value, fromUser ->
                if (fromUser) {
                    viewModel.setMaxListen(value.toInt())
                }
            }

            maxRepeatSlider.addOnChangeListener { _, value, fromUser ->
                if (fromUser) {
                    viewModel.setMaxRepeat(value.toInt())
                }
            }

            numbersSessionSizeSlider.addOnChangeListener { _, value, fromUser ->
                if (fromUser) {
                    viewModel.setNumbersSessionSize(value.toInt())
                }
            }
        }
    }

    private fun observeSettings() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.settings.collect { settings ->
                    with(binding) {
                        maxListenSlider.value = settings.maxListen.toFloat()
                        maxRepeatSlider.value = settings.maxRepeat.toFloat()
                        numbersSessionSizeSlider.value = settings.numbersSessionSize.toFloat()
                        maxListenValue.text = settings.maxListen.toString()
                        maxRepeatValue.text = settings.maxRepeat.toString()
                        numbersSessionSizeValue.text = settings.numbersSessionSize.toString()
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