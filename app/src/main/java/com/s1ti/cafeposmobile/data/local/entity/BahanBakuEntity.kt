package com.s1ti.cafeposmobile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.s1ti.cafeposmobile.data.model.StokStatus

/**
 * Tabel bahan baku -- SRS 3.4 Manajemen Stok/Persediaan, REQ-3.1 s.d REQ-3.12.
 *
 * Catatan:
 * - `statusStok` dihitung dari stokSaatIni vs stokMinimal, BUKAN kolom
 *   tersimpan (getter custom di luar constructor -> Room otomatis
 *   mengabaikannya, gak butuh @Ignore).
 * - REQ-3.5 (kurangi stok otomatis pas transaksi) & REQ-3.7 (tolak transaksi
 *   kalau stok gak cukup) belum diimplementasikan di sini -- itu baru bisa
 *   jalan setelah fitur Pemesanan/Pembayaran (SRS 3.5-3.6) ada dan ada tabel
 *   resep yang menghubungkan menu ke bahan baku (BOM). Untuk sekarang,
 *   Manajemen Stok berdiri sendiri dulu (CRUD manual).
 */
@Entity(tableName = "bahan_baku")
data class BahanBakuEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nama: String,
    val satuan: String, // contoh: "kg", "liter", "pcs"
    val stokSaatIni: Double,
    val stokMinimal: Double,
    val hargaBeli: Long? = null,
    val supplier: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val statusStok: StokStatus
        get() = when {
            stokSaatIni <= 0.0 -> StokStatus.HABIS
            stokSaatIni <= stokMinimal -> StokStatus.MENIPIS
            else -> StokStatus.AMAN
        }
}
