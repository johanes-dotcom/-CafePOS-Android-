package com.s1ti.cafeposmobile.ui.screens.dashboard

import androidx.compose.ui.graphics.Color
import com.s1ti.cafeposmobile.ui.screens.dashboard.components.BarChartEntry
import com.s1ti.cafeposmobile.ui.screens.dashboard.components.DonutSlice

/**
 * Data dummy utk Dashboard Analytics (rancangan antarmuka D.2 frame
 * "Dashboard Analytics"). Belum ada REQ tersendiri di SRS 3.x -- ini
 * pelengkap fitur Dashboard (SRS 3.1).
 * TODO: ganti dgn agregasi asli begitu Riwayat Transaksi & Laporan
 * (SRS 3.7-3.8) sudah dibangun.
 */
data class AnalyticsData(
    val periodeLabel: String = "14 - 20 Mei 2025",
    val totalPenjualan: Long = 56_480_000,
    val deltaPercent: Double = 18.6,
    val penjualanPerHari: List<BarChartEntry> = listOf(
        BarChartEntry("Sen", 6.2f),
        BarChartEntry("Sel", 7.8f),
        BarChartEntry("Rab", 5.4f),
        BarChartEntry("Kam", 9.1f),
        BarChartEntry("Jum", 10.3f),
        BarChartEntry("Sab", 12.0f),
        BarChartEntry("Min", 5.6f)
    ),
    val penjualanPerKategori: List<DonutSlice> = listOf(
        DonutSlice("Makanan", 45f, Color(0xFF2E7D32)),   // Primary Green
        DonutSlice("Minuman", 30f, Color(0xFF388E3C)),   // Secondary Green
        DonutSlice("Snack", 15f, Color(0xFF43A047)),     // Success Green
        DonutSlice("Lainnya", 10f, Color(0xFFA5D6A7))    // Light Green
    ),
    val produkTerlaris: String = "Cafe Latte",
    val produkTerlarisJumlah: Int = 120
)
