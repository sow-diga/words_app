package com.mas.quranwords.domain.extensions

import android.widget.TextView
import androidx.core.view.isVisible

fun TextView.showIfNotBlank(text: String?) {
    isVisible = !text.isNullOrBlank()
    if (isVisible) {
        this.text = text
    }
}