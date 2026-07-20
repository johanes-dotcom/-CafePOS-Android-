package com.s1ti.cafeposmobile.ui.cashier

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.s1ti.cafeposmobile.data.local.AppDatabase
import com.s1ti.cafeposmobile.data.repository.TransactionRepository

class PaymentViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val database = AppDatabase.getDatabase(context)
        val repository = TransactionRepository(database.transactionDao())
        @Suppress("UNCHECKED_CAST")
        return PaymentViewModel(repository) as T
    }
}
