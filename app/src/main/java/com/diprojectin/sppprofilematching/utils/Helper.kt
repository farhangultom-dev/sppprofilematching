package com.diprojectin.sppprofilematching.utils

import android.widget.EditText
import java.text.DecimalFormat

object Helper {
    fun Number?.formatCurrency(): String {
        if (this == null) return "Rp 0"
        val df = DecimalFormat("#,###,###.##")
        return String.format("Rp %s", df.format(this))
    }

    fun Number?.formatCurrencyWithoutRp(): String {
        if (this == null) return "0"
        val df = DecimalFormat("#,###,###.##")
        return String.format("%s", df.format(this))
    }

    fun getCleanCurrencyValue(editText: EditText): Long {
        val raw = editText.text.toString()
            .replace("Rp", "")
            .replace(".", "")
            .replace(",", "")
            .replace("\\s".toRegex(), "")

        return raw.toLongOrNull() ?: 0L
    }
}