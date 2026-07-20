package com.s1ti.cafeposmobile.ui.cashier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s1ti.cafeposmobile.data.repository.TransactionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class DashboardStats(
    val totalRevenue: Double = 0.0,
    val totalTransactions: Int = 0,
    val totalProfit: Double = 0.0,
    val averageOrder: Double = 0.0
)

class DashboardViewModel(private val repository: TransactionRepository) : ViewModel() {

    val stats: StateFlow<DashboardStats> = repository.allTransactions
        .map { transactions ->
            val successfulTransactions = transactions.filter { it.status == "Selesai" }
            val totalRevenue = successfulTransactions.sumOf { it.totalAmount }
            val totalTransactions = successfulTransactions.size
            val totalProfit = successfulTransactions.sumOf { it.totalAmount - it.totalCostPrice }
            val averageOrder = if (totalTransactions > 0) totalRevenue / totalTransactions else 0.0
            
            DashboardStats(
                totalRevenue = totalRevenue,
                totalTransactions = totalTransactions,
                totalProfit = totalProfit,
                averageOrder = averageOrder
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DashboardStats()
        )
}
