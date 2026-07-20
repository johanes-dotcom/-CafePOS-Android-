package com.s1ti.cafeposmobile.data.repository

import com.s1ti.cafeposmobile.data.local.dao.MenuDao
import com.s1ti.cafeposmobile.data.local.entity.MenuEntity
import com.s1ti.cafeposmobile.data.model.MenuKategori
import kotlinx.coroutines.flow.Flow

class MenuRepository(private val menuDao: MenuDao) {

    sealed class SaveResult {
        object Success : SaveResult()
        data class ValidationError(val message: String) : SaveResult()
        data class Failure(val message: String) : SaveResult()
    }

    fun getAllMenus(): Flow<List<MenuEntity>> = menuDao.getAllMenus()

    suspend fun getMenuById(id: Long): MenuEntity? = menuDao.getMenuById(id)

    /**
     * REQ-2.2 (tambah baru kalau id == null), REQ-2.3 (ubah kalau id != null),
     * REQ-2.5 (validasi: nama tidak kosong, harga numerik positif).
     */
    suspend fun saveMenu(
        id: Long?,
        nama: String,
        kategori: MenuKategori,
        hargaJualInput: String,
        hargaModalInput: String,
        deskripsi: String,
        fotoUri: String?,
        tersedia: Boolean
    ): SaveResult {
        if (nama.isBlank()) {
            return SaveResult.ValidationError("Nama menu tidak boleh kosong.")
        }
        val hargaJual = hargaJualInput.toLongOrNull()
        if (hargaJual == null || hargaJual <= 0) {
            return SaveResult.ValidationError("Harga jual harus berupa angka positif.")
        }
        val hargaModal = hargaModalInput.toLongOrNull()

        return try {
            val now = System.currentTimeMillis()
            if (id == null) {
                menuDao.insertMenu(
                    MenuEntity(
                        nama = nama.trim(),
                        kategori = kategori,
                        hargaJual = hargaJual,
                        hargaModal = hargaModal,
                        deskripsi = deskripsi.trim(),
                        fotoUri = fotoUri,
                        tersedia = tersedia,
                        createdAt = now,
                        updatedAt = now
                    )
                )
            } else {
                val existing = menuDao.getMenuById(id)
                    ?: return SaveResult.Failure("Menu tidak ditemukan.")
                menuDao.updateMenu(
                    existing.copy(
                        nama = nama.trim(),
                        kategori = kategori,
                        hargaJual = hargaJual,
                        hargaModal = hargaModal,
                        deskripsi = deskripsi.trim(),
                        fotoUri = fotoUri,
                        tersedia = tersedia,
                        updatedAt = now
                    )
                )
            }
            SaveResult.Success
        } catch (e: Exception) {
            // REQ-2.12
            SaveResult.Failure("Gagal menyimpan menu: ${e.message}")
        }
    }

    // REQ-2.4
    suspend fun deleteMenu(menu: MenuEntity) {
        menuDao.deleteMenu(menu)
    }
}
