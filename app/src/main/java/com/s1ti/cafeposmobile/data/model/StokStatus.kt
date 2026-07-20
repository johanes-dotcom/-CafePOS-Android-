package com.s1ti.cafeposmobile.data.model

/**
 * REQ-3.1 ("status stok"), REQ-3.6 (notifikasi stok menipis/habis).
 * Ini status hasil hitungan (bukan kolom tersimpan) -- lihat
 * BahanBakuEntity.statusStok.
 */
enum class StokStatus(val label: String) {
    AMAN("Aman"),
    MENIPIS("Menipis"),
    HABIS("Habis")
}
