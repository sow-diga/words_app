package com.mas.quranwords.ui;

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.mas.quranwords.R
import com.mas.quranwords.databinding.ActivityDetailBinding
import com.mas.quranwords.models.ItemType
import com.mas.quranwords.util.EXTRA_TYPE

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val type = intent.getStringExtra(EXTRA_TYPE)

        val navController = (supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
            .navController

        val graph = navController.navInflater.inflate(R.navigation.nav_graph)

        graph.setStartDestination(
            when (type) {
                ItemType.WORD.name -> R.id.listFragment
                ItemType.MEMORIZE.name -> R.id.listFragment
                ItemType.MISTAKE.name -> R.id.listFragment
                ItemType.NUMBERS.name -> R.id.numbersFragment
                else -> R.id.listFragment
            }
        )

        navController.graph = graph
    }
}