package com.s1ti.cafeposmobile.ui.cashier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s1ti.cafeposmobile.data.local.entity.MenuEntity
import com.s1ti.cafeposmobile.data.model.MenuKategori
import com.s1ti.cafeposmobile.data.repository.MenuRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductMenuViewModel(private val repository: MenuRepository) : ViewModel() {

    val allMenus: StateFlow<List<MenuEntity>> = repository.getAllMenus()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveMenu(
        id: Long?,
        nama: String,
        kategori: String,
        hargaJual: String,
        hargaModal: String,
        deskripsi: String,
        fotoUri: String?,
        tersedia: Boolean
    ) {
        viewModelScope.launch {
            val menuKategori = when (kategori) {
                "Coffee" -> MenuKategori.MINUMAN // Map appropriately or use string directly if repo supports
                "Drink" -> MenuKategori.MINUMAN
                "Food" -> MenuKategori.MAKANAN
                "Snack" -> MenuKategori.SNACK
                else -> MenuKategori.MAKANAN
            }
            repository.saveMenu(id, nama, menuKategori, hargaJual, hargaModal, deskripsi, fotoUri, tersedia)
        }
    }

    fun deleteMenu(menu: MenuEntity) {
        viewModelScope.launch {
            repository.deleteMenu(menu)
        }
    }
}
