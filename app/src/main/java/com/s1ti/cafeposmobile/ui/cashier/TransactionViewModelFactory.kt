package com.s1ti.cafeposmobile.ui.cashier

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.s1ti.cafeposmobile.data.local.AppDatabase
import com.s1ti.cafeposmobile.data.repository.MenuRepository

class TransactionViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            val database = AppDatabase.getDatabase(context)
            val repository = MenuRepository(database.menuDao())
            @Suppress("UNCHECKED_CAST")
            return TransactionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
