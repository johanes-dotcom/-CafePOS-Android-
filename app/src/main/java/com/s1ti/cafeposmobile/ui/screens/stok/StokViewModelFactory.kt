package com.s1ti.cafeposmobile.ui.screens.stok

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.s1ti.cafeposmobile.data.local.AppDatabase
import com.s1ti.cafeposmobile.data.repository.StokRepository

class StokViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dao = AppDatabase.getDatabase(context).bahanBakuDao()
        val repository = StokRepository(dao)
        @Suppress("UNCHECKED_CAST")
        return StokViewModel(repository) as T
    }
}
