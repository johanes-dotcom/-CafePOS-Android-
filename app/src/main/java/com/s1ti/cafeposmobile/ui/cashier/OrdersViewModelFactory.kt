package com.s1ti.cafeposmobile.ui.cashier

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.s1ti.cafeposmobile.data.local.AppDatabase
import com.s1ti.cafeposmobile.data.repository.TransactionRepository

class OrdersViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val database = AppDatabase.getDatabase(context)
        val repository = TransactionRepository(database.transactionDao())
        
        return when {
            modelClass.isAssignableFrom(OrdersViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                OrdersViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ReportViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                ReportViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
