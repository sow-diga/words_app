package com.mas.quranwords.ui.adapter

import android.content.Context
import android.widget.ArrayAdapter
import com.mas.quranwords.R
import com.mas.quranwords.qari.Reciter

class ReciterAdapter(
    context: Context,
    reciters: List<Reciter>
) : ArrayAdapter<Reciter>(
    context,
    R.layout.item_reciter,
    R.id.itemReciterName,
    reciters
)