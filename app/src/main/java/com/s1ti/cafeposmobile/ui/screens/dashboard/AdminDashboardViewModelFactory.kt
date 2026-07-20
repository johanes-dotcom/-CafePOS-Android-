package com.s1ti.cafeposmobile.ui.screens.dashboard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.s1ti.cafeposmobile.data.local.AppDatabase
import com.s1ti.cafeposmobile.data.repository.TransactionRepository

class AdminDashboardViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val database = AppDatabase.getDatabase(context)
        val repository = TransactionRepository(database.transactionDao())
        
        return when {
            modelClass.isAssignableFrom(AdminDashboardViewModel::class.java) -> {
                AdminDashboardViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AdminReportViewModel::class.java) -> {
                AdminReportViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
