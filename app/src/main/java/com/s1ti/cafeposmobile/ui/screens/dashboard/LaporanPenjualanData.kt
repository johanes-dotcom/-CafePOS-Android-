package com.s1ti.cafeposmobile.ui.screens.dashboard

import com.s1ti.cafeposmobile.data.local.entity.TransactionEntity

enum class PeriodeLaporan(val label: String) {
    HARIAN("Hari Ini"),
    KEMARIN("Kemarin"),
    MINGGUAN("Minggu Ini"),
    BULANAN("Bulan Ini"),
    TAHUNAN("Tahun Ini")
}

data class MetodePembayaranRingkasan(
    val metode: String,
    val nominal: Long,
    val persentase: Int
)

data class MenuTerlaris(
    val nama: String,
    val jumlahTerjual: Int
)

data class LaporanPenjualanData(
    val periode: PeriodeLaporan,
    val tanggalLabel: String,
    val totalPenjualan: Long,
    val deltaPercent: Double,
    val totalTransaksi: Int,
    val totalDiskon: Long,
    val totalPajak: Long,
    val totalService: Long,
    val totalPembayaran: Long,
    val totalProfitEstimasi: Long,
    val metodePembayaran: List<MetodePembayaranRingkasan>,
    val menuTerlaris: List<MenuTerlaris>,
    val daftarTransaksi: List<TransactionEntity> = emptyList()
) {
    val isEmpty: Boolean get() = totalTransaksi == 0
}
