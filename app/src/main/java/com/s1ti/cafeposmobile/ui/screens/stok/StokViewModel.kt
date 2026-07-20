package com.s1ti.cafeposmobile.ui.screens.stok

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s1ti.cafeposmobile.data.local.entity.BahanBakuEntity
import com.s1ti.cafeposmobile.data.model.StokStatus
import com.s1ti.cafeposmobile.data.repository.StokRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StokViewModel(
    private val repository: StokRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // REQ-3.1, REQ-3.8: daftar bahan baku + pencarian, reaktif dari DB
    val filteredBahanBaku: StateFlow<List<BahanBakuEntity>> = combine(
        repository.getAllBahanBaku(), _searchQuery
    ) { list, query ->
        if (query.isBlank()) list else list.filter { it.nama.contains(query, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // REQ-3.6: bahan yang stoknya menipis/habis, buat banner peringatan
    val lowStockItems: StateFlow<List<BahanBakuEntity>> = repository.getAllBahanBaku()
        .map { list -> list.filter { it.statusStok != StokStatus.AMAN } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _saveResult = MutableStateFlow<StokRepository.SaveResult?>(null)
    val saveResult: StateFlow<StokRepository.SaveResult?> = _saveResult.asStateFlow()

    fun onSearchChange(query: String) {
        _searchQuery.value = query
    }

    fun saveBahanBaku(
        id: Long?,
        nama: String,
        satuan: String,
        stokSaatIni: String,
        stokMinimal: String,
        hargaBeli: String,
        supplier: String
    ) {
        viewModelScope.launch {
            _saveResult.value = repository.saveBahanBaku(
                id, nama, satuan, stokSaatIni, stokMinimal, hargaBeli, supplier
            )
        }
    }

    fun consumeSaveResult() {
        _saveResult.value = null
    }

    fun deleteBahanBaku(bahan: BahanBakuEntity) {
        viewModelScope.launch { repository.deleteBahanBaku(bahan) }
    }
}
