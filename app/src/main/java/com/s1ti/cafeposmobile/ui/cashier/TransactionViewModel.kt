package com.s1ti.cafeposmobile.ui.cashier

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s1ti.cafeposmobile.data.Product
import com.s1ti.cafeposmobile.data.repository.MenuRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TransactionViewModel(private val repository: MenuRepository) : ViewModel() {

    val availableProducts: StateFlow<List<Product>> = repository.getAllMenus()
        .map { menus ->
            menus.filter { it.tersedia }.map { menu ->
                Product(
                    id = menu.id.toString(),
                    name = menu.nama,
                    price = menu.hargaJual.toDouble(),
                    category = menu.kategori.name,
                    description = menu.deskripsi,
                    imageUri = menu.fotoUri?.let { Uri.parse(it) },
                    isAvailable = menu.tersedia,
                    costPrice = menu.hargaModal?.toDouble() ?: 0.0
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
