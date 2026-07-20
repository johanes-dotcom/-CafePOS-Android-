package com.s1ti.cafeposmobile.util

import java.text.NumberFormat
import java.util.Locale

/**
 * Kebutuhan Lain (SRS): "Seluruh nilai mata uang pada aplikasi harus
 * menggunakan format Rupiah (Rp) dengan penulisan angka mengikuti standar
 * Indonesia, misalnya Rp25.000."
 */
object CurrencyFormatter {
    private val idLocale = Locale("in", "ID")
    private val formatter = NumberFormat.getNumberInstance(idLocale)

    fun formatRupiah(amount: Long): String = "Rp" + formatter.format(amount)

    fun formatRupiah(amount: Double): String = formatRupiah(amount.toLong())
}
