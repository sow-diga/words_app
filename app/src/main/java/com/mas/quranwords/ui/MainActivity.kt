package com.mas.quranwords.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mas.quranwords.databinding.ActivityMainBinding
import com.mas.quranwords.ui.adapter.WordAdapter
import com.mas.quranwords.util.EXTRA_WORD

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: WordAdapter

    private val viewModel: WordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeData()

        viewModel.fetchWords()
    }


    private fun setupRecyclerView() {
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerView.addItemDecoration(
                DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL)
            )
            adapter =  WordAdapter { word ->
                val intent = Intent(this@MainActivity, WordActivity::class.java)
                intent.putExtra(EXTRA_WORD, word)
                startActivity(intent)
            }
            recyclerView.adapter = adapter
        }
    }

    private fun observeData() {
        viewModel.words.observe(this) { wordList ->
            adapter.submitList(wordList)
        }

        viewModel.errorMessage.observe(this) { errorMsg ->

        }
    }

}