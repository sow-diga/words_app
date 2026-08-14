package com.mas.quranwords.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mas.quranwords.R
import com.mas.quranwords.data.settings.SettingsRepository
import com.mas.quranwords.databinding.FragmentSettingsBinding
import com.mas.quranwords.qari.Reciters
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
        setupPracticeReciters()
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

    private fun setupPracticeReciters() {
        val reciters = Reciters.RECITERS_ONLY
        val names = reciters.map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, names)

        binding.practiceReciter1Dropdown.apply {
            setAdapter(adapter)
            setOnItemClickListener { _, _, position, _ ->
                val reciter = reciters[position]
                viewModel.setPracticeReciter1(reciter.folder!!)
            }
        }

        binding.practiceReciter2Dropdown.apply {
            setAdapter(adapter)

            setOnItemClickListener { _, _, position, _ ->
                val reciter = reciters[position]
                viewModel.setPracticeReciter2(reciter.folder!!)
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

                        practiceReciter1Dropdown.setText(
                            Reciters.RECITERS_ONLY
                                .firstOrNull { it.folder == settings.practiceReciter1 }
                                ?.name ?: Reciters.AYMAN_SUWAID.name,
                            false
                        )

                        practiceReciter2Dropdown.setText(
                            Reciters.RECITERS_ONLY
                                .firstOrNull { it.folder == settings.practiceReciter2 }
                                ?.name ?: Reciters.HUSARY.name,
                            false
                        )
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