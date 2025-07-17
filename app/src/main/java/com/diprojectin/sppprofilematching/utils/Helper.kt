package com.diprojectin.sppprofilematching.utils

import android.content.res.Resources
import android.widget.EditText
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

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

    val Int.dp: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

    fun String?.convertToIndonesianDate(): String {
        // 1. Parse the original string
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dateTime = LocalDateTime.parse(this, inputFormatter)

        // 2. Format with Indonesian locale
        val outputFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        return dateTime.format(outputFormatter)
    }
}