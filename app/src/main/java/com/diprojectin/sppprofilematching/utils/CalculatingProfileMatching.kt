package com.diprojectin.sppprofilematching.utils

import android.content.Context

data class Kriteria(
    val nama: String,
    val jenis: JenisKriteria, // CORE atau SECONDARY
    val nilaiMahasiswa: Int,
    val nilaiStandar: Int
)

enum class JenisKriteria {
    CORE, SECONDARY
}

class CalculatingProfileMatching(
    private val kriteriaList: List<Kriteria>
) {
    // Tabel konversi gap ke skor
    private fun konversiGap(gap: Int): Double {
        return when (gap) {
            0 -> 5.0
            1 -> 4.5
            -1 -> 4.0
            2 -> 3.5
            -2 -> 3.0
            3 -> 2.5
            -3 -> 2.0
            4 -> 1.5
            -4 -> 1.0
            else -> 0.0
        }
    }

    fun hitungNilaiAkhir(): Double {
        val coreScores = mutableListOf<Double>()
        val secondaryScores = mutableListOf<Double>()

        for (kriteria in kriteriaList) {
            val gap = kriteria.nilaiMahasiswa - kriteria.nilaiStandar
            val skor = konversiGap(gap)

            if (kriteria.jenis == JenisKriteria.CORE) {
                coreScores.add(skor)
            } else {
                secondaryScores.add(skor)
            }
        }

        val rataCore = coreScores.average()
        val rataSecondary = secondaryScores.average()

        return (rataCore * 0.6) + (rataSecondary * 0.4)
    }
}