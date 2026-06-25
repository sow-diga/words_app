package com.mas.quranwords.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mas.quranwords.databinding.ActivityMainBinding
import com.mas.quranwords.models.Category
import com.mas.quranwords.models.ItemType
import com.mas.quranwords.ui.adapter.CategoryAdapter
import com.mas.quranwords.ui.adapter.WordAdapter
import com.mas.quranwords.util.EXTRA_TYPE

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: WordAdapter

    private val viewModel: WordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
    }


    private fun setupRecyclerView() {
        binding.apply {

            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerView.addItemDecoration(
                DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL)
            )

            val categories = loadCategories()
            val adapter = CategoryAdapter(categories) { category ->

                val intent = Intent(this@MainActivity, DetailActivity::class.java)

                intent.putExtra(EXTRA_TYPE, category.type.name)

                startActivity(intent)
            }
            recyclerView.adapter = adapter
        }
    }

    private fun loadCategories(): List<Category> {
        return listOf(
            Category("Difficult Words", ItemType.WORD),
            Category("Memorizing", ItemType.MEMORIZE),
            Category("Mistakes", ItemType.MISTAKE)
        )
    }

}