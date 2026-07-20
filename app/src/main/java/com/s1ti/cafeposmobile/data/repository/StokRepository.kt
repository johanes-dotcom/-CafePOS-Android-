package com.s1ti.cafeposmobile.data.repository

import com.s1ti.cafeposmobile.data.local.dao.BahanBakuDao
import com.s1ti.cafeposmobile.data.local.entity.BahanBakuEntity
import kotlinx.coroutines.flow.Flow

class StokRepository(private val dao: BahanBakuDao) {

    sealed class SaveResult {
        object Success : SaveResult()
        data class ValidationError(val message: String) : SaveResult()
        data class Failure(val message: String) : SaveResult()
    }

    fun getAllBahanBaku(): Flow<List<BahanBakuEntity>> = dao.getAllBahanBaku()

    suspend fun getBahanBakuById(id: Long): BahanBakuEntity? = dao.getBahanBakuById(id)

    /**
     * REQ-3.2 (tambah baru kalau id == null), REQ-3.3 & REQ-3.9 (ubah kalau
     * id != null, termasuk ubah batas minimum), validasi input numerik.
     */
    suspend fun saveBahanBaku(
        id: Long?,
        nama: String,
        satuan: String,
        stokSaatIniInput: String,
        stokMinimalInput: String,
        hargaBeliInput: String,
        supplier: String
    ): SaveResult {
        if (nama.isBlank()) {
            return SaveResult.ValidationError("Nama bahan baku tidak boleh kosong.")
        }
        if (satuan.isBlank()) {
            return SaveResult.ValidationError("Satuan tidak boleh kosong.")
        }
        val stokSaatIni = stokSaatIniInput.replace(",", ".").toDoubleOrNull()
        if (stokSaatIni == null || stokSaatIni < 0.0) {
            return SaveResult.ValidationError("Stok saat ini harus berupa angka 0 atau lebih.")
        }
        val stokMinimal = stokMinimalInput.replace(",", ".").toDoubleOrNull()
        if (stokMinimal == null || stokMinimal < 0.0) {
            return SaveResult.ValidationError("Stok minimal harus berupa angka 0 atau lebih.")
        }
        val hargaBeli = hargaBeliInput.toLongOrNull()

        return try {
            val now = System.currentTimeMillis()
            if (id == null) {
                dao.insertBahanBaku(
                    BahanBakuEntity(
                        nama = nama.trim(),
                        satuan = satuan.trim(),
                        stokSaatIni = stokSaatIni,
                        stokMinimal = stokMinimal,
                        hargaBeli = hargaBeli,
                        supplier = supplier.trim().ifBlank { null },
                        createdAt = now,
                        updatedAt = now
                    )
                )
            } else {
                val existing = dao.getBahanBakuById(id)
                    ?: return SaveResult.Failure("Bahan baku tidak ditemukan.")
                dao.updateBahanBaku(
                    existing.copy(
                        nama = nama.trim(),
                        satuan = satuan.trim(),
                        stokSaatIni = stokSaatIni,
                        stokMinimal = stokMinimal,
                        hargaBeli = hargaBeli,
                        supplier = supplier.trim().ifBlank { null },
                        updatedAt = now
                    )
                )
            }
            SaveResult.Success
        } catch (e: Exception) {
            // REQ-3.12
            SaveResult.Failure("Gagal menyimpan bahan baku: ${e.message}")
        }
    }

    // REQ-3.4
    suspend fun deleteBahanBaku(bahan: BahanBakuEntity) {
        dao.deleteBahanBaku(bahan)
    }
}
