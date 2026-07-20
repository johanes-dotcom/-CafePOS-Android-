package com.s1ti.cafeposmobile.ui.screens.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s1ti.cafeposmobile.data.local.entity.MenuEntity
import com.s1ti.cafeposmobile.data.model.MenuKategori
import com.s1ti.cafeposmobile.data.repository.MenuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MenuViewModel(
    private val repository: MenuRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedKategori = MutableStateFlow<MenuKategori?>(null) // null = Semua
    val selectedKategori: StateFlow<MenuKategori?> = _selectedKategori.asStateFlow()

    // REQ-2.1, REQ-2.7, REQ-2.8: daftar menu + pencarian + filter kategori, reaktif dari DB
    val filteredMenus: StateFlow<List<MenuEntity>> = combine(
        repository.getAllMenus(), _searchQuery, _selectedKategori
    ) { menus, query, kategori ->
        menus.filter { menu ->
            (kategori == null || menu.kategori == kategori) &&
                (query.isBlank() || menu.nama.contains(query, ignoreCase = true))
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _saveResult = MutableStateFlow<MenuRepository.SaveResult?>(null)
    val saveResult: StateFlow<MenuRepository.SaveResult?> = _saveResult.asStateFlow()

    fun onSearchChange(query: String) {
        _searchQuery.value = query
    }

    fun onKategoriChange(kategori: MenuKategori?) {
        _selectedKategori.value = kategori
    }

    fun saveMenu(
        id: Long?,
        nama: String,
        kategori: MenuKategori,
        hargaJual: String,
        hargaModal: String,
        deskripsi: String,
        fotoUri: String?,
        tersedia: Boolean
    ) {
        viewModelScope.launch {
            _saveResult.value = repository.saveMenu(
                id, nama, kategori, hargaJual, hargaModal, deskripsi, fotoUri, tersedia
            )
        }
    }

    fun consumeSaveResult() {
        _saveResult.value = null
    }

    fun deleteMenu(menu: MenuEntity) {
        viewModelScope.launch { repository.deleteMenu(menu) }
    }
}
