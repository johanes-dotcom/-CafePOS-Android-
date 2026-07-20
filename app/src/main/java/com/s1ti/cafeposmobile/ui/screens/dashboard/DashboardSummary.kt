package com.s1ti.cafeposmobile.ui.screens.dashboard

/**
 * Data dummy sementara untuk scaffolding UI Dashboard Admin/Owner.
 * TODO: ganti dengan data asli hasil agregasi Riwayat Transaksi & Laporan
 * (SRS 3.7 Riwayat Transaksi & 3.8 Laporan Harian) begitu fitur Pemesanan,
 * Pembayaran, dan Transaksi sudah dibangun.
 */
data class DashboardSummary(
    val totalPenjualanHariIni: Long = 8_250_000,
    val totalPenjualanDeltaPercent: Double = 12.8,
    val totalTransaksi: Int = 128,
    val totalTransaksiDeltaPercent: Double = 6.5,
    val totalProfit: Long = 3_125_000,
    val totalProfitDeltaPercent: Double = 15.3,
    val averageOrder: Long = 64_453,
    val averageOrderDeltaPercent: Double = 6.1,
    val grafikPenjualanHarian: List<Float> = listOf(2f, 3f, 2.6f, 4.1f, 3.6f, 5.2f, 4.8f, 6f)
)
